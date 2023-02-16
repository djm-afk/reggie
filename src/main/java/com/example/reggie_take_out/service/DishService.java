package com.example.reggie_take_out.service;

import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.respR.Result;

import java.util.ArrayList;
import java.util.List;

/**
* @author 32580
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-02-07 11:10:03
*/
public interface DishService extends IService<Dish> {

    // 新增菜品，并插入菜品口味；操作两张表
    boolean saveWithFlavor(DishDto dishDto);

    // 根据id查菜品和对应口味信息
    DishDto getByIdWithFlavor(Long id);

    // 更新菜品信息，同时更新对应的口味信息
    boolean updateWithFlavor(DishDto dishDto);

    // 删除菜品
    boolean deleteByIdWithFlavor(Long[] ids);


}
