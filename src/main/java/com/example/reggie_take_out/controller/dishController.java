package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.pojo.Category;
import com.example.reggie_take_out.pojo.Dish;
import com.example.reggie_take_out.pojo.DishFlavor;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.DishFlavorService;
import com.example.reggie_take_out.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@ResponseBody
@RequestMapping("/dish")
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class dishController {

    @Autowired
    private DishService ds;
    @Autowired
    private CategoryService cs;
    @Autowired
    private DishFlavorService dls;
    @Autowired
    private RedisTemplate redisTemplate;

    // id查询菜品
    @GetMapping("{id}")
    public Result<DishDto> selectById(@PathVariable Long id){
        DishDto byIdWithFlavor = ds.getByIdWithFlavor(id);
        return Result.success(byIdWithFlavor);
    }

    // 分页查询菜品分类
    @GetMapping("/page")
    public Result<Page> selectByPage(@PathParam("name") String name,@PathParam("page") Integer page, @PathParam("pageSize") Integer pageSize){
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.like(!Strings.isEmpty(name),Dish::getName,name);
        lqw.orderByDesc(Dish::getCreateTime);
        Page page_ = ds.page(new Page(page, pageSize), lqw);
        long pages = page_.getPages();
        if (pages < page){
            page_ = ds.page(new Page(pages, pageSize), lqw);
        }

        // 对象拷贝
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(page_,dishDtoPage,"records");
        // 处理records
        List<Dish> records = page_.getRecords();
        List<DishDto> list = new ArrayList<>();
        for (Dish record : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record,dishDto);
            Category category = cs.getById(record.getCategoryId());
            dishDto.setCategoryName(category.getName());
            list.add(dishDto);
        }
        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    // 新增菜品
    @PostMapping()
    public Result<String> save(@RequestBody DishDto dishDto){
        boolean save = ds.saveWithFlavor(dishDto);
        return save ? Result.success("保存成功！") : Result.error("保存失败！");
    }

    // 修改菜品
    @PutMapping()
    public Result<String> update(@RequestBody DishDto dishDto){
        boolean update = ds.updateWithFlavor(dishDto);
        return update ? Result.success("修改成功！") : Result.error("修改失败！");
    }

    // 停/启售
    @PostMapping("/status/{status}")
    public Result<String> status(@PathParam("ids") Long[] ids,@PathVariable Integer status){
        List<Dish> dishList = new ArrayList<>();
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishList.add(dish);
        }
        boolean update = ds.updateBatchById(dishList);
        return update ? Result.success("保存成功！") : Result.error("保存失败！");
    }

    // 删除菜品
    @DeleteMapping()
    public Result<String> delete(@PathParam("ids") Long[] ids){
        boolean delete = ds.deleteByIdWithFlavor(ids);
        return delete ? Result.success("删除成功！") : Result.error("删除失败！");
    }

    // 查询菜品
    @GetMapping("/list")
    public Result<List<DishDto>> selectDishByCategoryId(@PathParam("categoryId") Dish dish){
        Long categoryId = dish.getCategoryId();
        List<DishDto> dishDtoList = null;

        String key = "categoryDish_" + categoryId;
        ValueOperations<String,List<DishDto>> valueOperations = redisTemplate.opsForValue();
        dishDtoList = valueOperations.get(key);
        if (!Objects.isNull(dishDtoList)){
            return Result.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getStatus,1);
        lqw.eq(Dish::getCategoryId,categoryId);
        List<Dish> dishList = ds.list(lqw);

        dishDtoList = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            LambdaQueryWrapper<DishFlavor> lqwdf = new LambdaQueryWrapper<>();
            lqwdf.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> list = dls.list(lqwdf);
            dishDto.setFlavors(list);
            return dishDto;
        }).collect(Collectors.toList());
        valueOperations.set(key,dishDtoList);
        return Result.success(dishDtoList);
    }
}
