package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.pojo.Category;
import com.example.reggie_take_out.pojo.Employee;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Objects;

@RestController
@ResponseBody
@RequestMapping("/category")
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class categoryController {

    @Autowired
    private CategoryService cs;

    // 新增分类
    @PostMapping()
    public Result<String> add(@RequestBody Category category){
        log.info(category.toString());
        boolean save = cs.save(category);
        return save ? Result.success("新增分类成功") : Result.error("新增分类失败");
    }

    // 分页查询菜品分类
    @GetMapping("/page")
    public Result<Page> selectByPage(@PathParam("page") Integer page, @PathParam("pageSize") Integer pageSize){
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper();
        lqw.orderByDesc(Category::getCreateTime);
        Page page_ = cs.page(new Page(page, pageSize), lqw);
        long pages = page_.getPages();
        System.out.println(pages);
        if (pages < page){
            page_ = cs.page(new Page(pages, pageSize), lqw);
        }
        return Result.success(page_);
    }

    // 删除分类
    @DeleteMapping()
    public Result<String> deleteCategory(@PathParam("ids") Long ids){

        boolean remove = cs.remove(ids);
        return remove ? Result.success("删除分类成功") : Result.error("删除分类失败");
    }

    // 修改分类
    @PutMapping()
    public Result<String> updateCategory(@RequestBody Category category){
        boolean update = cs.updateById(category);
        return update ? Result.success("修改分类成功") : Result.error("修改分类失败");
    }

    // 查询菜品分类 ?type=1/0
    @GetMapping("/list")
    public Result<List<Category>> selectCategory(@PathParam("type") Integer type){

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(!Objects.isNull(type),Category::getType,type);
        lqw.orderByAsc(Category::getSort);
        List<Category> list = cs.list(lqw);
        return list.isEmpty() ? Result.error("暂无菜品分类数据") : Result.success(list);
    }
}
