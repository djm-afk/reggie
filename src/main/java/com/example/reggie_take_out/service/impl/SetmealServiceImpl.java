package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.pojo.Dish;
import com.example.reggie_take_out.pojo.DishFlavor;
import com.example.reggie_take_out.pojo.Setmeal;
import com.example.reggie_take_out.pojo.SetmealDish;
import com.example.reggie_take_out.service.SetmealDishService;
import com.example.reggie_take_out.service.SetmealService;
import com.example.reggie_take_out.mapper.SetmealMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
* @author 32580
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{

    @Autowired
    private SetmealDishService sds;
    @Override
    @Transactional
    public boolean saveWithFlavor(SetmealDto setmealDto) {
        // 新增套餐
        boolean save = this.save(setmealDto);
        // 新增套餐菜品关系
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        boolean saveSetmealDishesBatch = sds.saveBatch(setmealDishes);
        return save && saveSetmealDishesBatch;
    }

    @Override
    public SetmealDto getByIdWithFlavor(Long id) {
        // 查询套餐
        Setmeal setmeal = this.getById(id);
        // 查询套餐对应的菜品列表
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = sds.list(lqw);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    @Override
    public boolean updateWithFlavor(SetmealDto setmealDto) {
        // 修改套餐表
        boolean updateSetmeal = this.updateById(setmealDto);
        // 修改套餐对应的菜品列表
        // 1、先删除原先套餐的菜品信息
        Long setmealDtoId = setmealDto.getId();
        LambdaUpdateWrapper<SetmealDish> luw = new LambdaUpdateWrapper<>();
        luw.eq(SetmealDish::getSetmealId,setmealDtoId);
        boolean removeSetmealDish = sds.remove(luw);
        // 2、在新增套餐的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealDtoId);
        });
        boolean saveSetmealDishes = sds.saveBatch(setmealDishes);
        return updateSetmeal && removeSetmealDish && saveSetmealDishes;
    }

    @Transactional
    public boolean deleteByIdWithFlavor(Long[] ids) {
        // 删除套餐
        boolean removeSetmeal = this.removeBatchByIds(Arrays.asList(ids));

        // 先删套餐对应的菜品列表
        Stream<Long> stream = Arrays.stream(ids);
        LambdaUpdateWrapper<SetmealDish> luw = new LambdaUpdateWrapper<>();
        stream.forEach(id -> {
            luw.eq(ids.length != 0,SetmealDish::getSetmealId,id);
            sds.remove(luw);
        });
        return removeSetmeal;
    }
}




