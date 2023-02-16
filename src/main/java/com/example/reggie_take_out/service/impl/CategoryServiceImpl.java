package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.controller.exceptionController.exception.categoryDelEx;
import com.example.reggie_take_out.pojo.Category;
import com.example.reggie_take_out.pojo.Dish;
import com.example.reggie_take_out.pojo.Setmeal;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.mapper.CategoryMapper;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 32580
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    public boolean remove(Long categoryId) {
        // 查询当前分类是否关联菜品
        LambdaQueryWrapper<Dish> lqwDish = new LambdaQueryWrapper();
        lqwDish.eq(Dish::getCategoryId,categoryId);
        long dishCount = dishService.count(lqwDish);
        if (dishCount > 0) {
            throw new categoryDelEx("当前分类关联了菜品，请先删除对于菜品");
        }
        // 查询当前分类是否关联套餐
        LambdaQueryWrapper<Setmeal> lqwSetmeal = new LambdaQueryWrapper();
        lqwSetmeal.eq(Setmeal::getCategoryId,categoryId);
        long setmealCount = setmealService.count(lqwSetmeal);
        if (setmealCount > 0) {
            throw new categoryDelEx("当前分类关联了套餐，请先删除对于套餐");
        }
        return super.removeById(categoryId);
    }
}




