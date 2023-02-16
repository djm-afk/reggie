package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.pojo.Category;
import com.example.reggie_take_out.pojo.Dish;
import com.example.reggie_take_out.pojo.DishFlavor;
import com.example.reggie_take_out.pojo.Setmeal;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.SetmealDishService;
import com.example.reggie_take_out.service.SetmealService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/setmeal")
@ResponseBody
@CrossOrigin(origins = "*",maxAge = 3600)
public class setmealController {

    @Autowired
    private SetmealService ss;
    @Autowired
    private CategoryService cs;

    // 新增套餐
    @PostMapping()
    public Result<String> saveSetmealDish(@RequestBody SetmealDto setmealDto){
        boolean save = ss.saveWithFlavor(setmealDto);
        return save ? Result.success("新增套餐成功") : Result.success("新增套餐失败");
    }

    // id查套餐
    @GetMapping("/{id}")
    public Result<SetmealDto> selectById(@PathVariable Long id){
        SetmealDto setmealDto = ss.getByIdWithFlavor(id);
        return Result.success(setmealDto);
    }

    // 修改套餐
    @PutMapping()
    public Result<String> update(@RequestBody SetmealDto setmealDto){
        boolean update = ss.updateWithFlavor(setmealDto);
        return update ? Result.success("修改成功！") : Result.error("修改失败！");
    }

    // 停/启售
    @PostMapping("/status/{status}")
    public Result<String> status(@PathParam("ids") Long[] ids,@PathVariable Integer status){
        List<Setmeal> setmealList = new ArrayList<>();
        Stream<Long> stream = Arrays.stream(ids);
        stream.forEach(id -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(status);
            setmealList.add(setmeal);
        });
        boolean update = ss.updateBatchById(setmealList);
        return update ? Result.success("保存成功！") : Result.error("保存失败！");
    }

    // 删除菜品
    @DeleteMapping()
    public Result<String> delete(@PathParam("ids") Long[] ids){
        boolean delete = ss.deleteByIdWithFlavor(ids);
        return delete ? Result.success("删除成功！") : Result.error("删除失败！");
    }


    // 分页查询套餐分类
    @GetMapping("/page")
    public Result<Page> selectByPage(@PathParam("name") String name, @PathParam("page") Integer page, @PathParam("pageSize") Integer pageSize){
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(!Strings.isEmpty(name), Setmeal::getName,name);
        lqw.orderByDesc(Setmeal::getCreateTime);
        Page<Setmeal> page_ = ss.page(new Page<>(page, pageSize), lqw);
        long pages = page_.getPages();
        if (pages < page){
            page_ = ss.page(new Page(pages, pageSize), lqw);
        }
        // 对象拷贝
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(page_,setmealDtoPage,"records");
        // 处理records
        List<Setmeal> records = page_.getRecords();
        List<SetmealDto> list = new ArrayList<>();
        records.stream().forEach(record -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record,setmealDto);
            Category category = cs.getById(record.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            list.add(setmealDto);
        });
        setmealDtoPage.setRecords(list);
        return Result.success(setmealDtoPage);
    }

    // front
    // 查询套餐数据
    @GetMapping("/list")
    public Result<List<Setmeal>> selectDishByCategoryId(@PathParam("categoryId") Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lqw =new LambdaQueryWrapper<>();
        lqw.eq(!Objects.isNull(setmeal.getCategoryId()),Setmeal::getCategoryId,setmeal.getCategoryId());
        lqw.eq(!Objects.isNull(setmeal.getStatus()),Setmeal::getStatus,setmeal.getStatus());
        lqw.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = ss.list(lqw);

        return Result.success(setmealList);
    }


}
