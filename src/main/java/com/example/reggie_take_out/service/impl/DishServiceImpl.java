package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.pojo.Dish;
import com.example.reggie_take_out.pojo.DishFlavor;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.DishFlavorService;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
* @author 32580
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

    @Autowired
    private DishFlavorService dfs;

    @Override
    @Transactional
    public boolean saveWithFlavor(DishDto dishDto) {
        // 保存基本信息到dish
        boolean saveDish = this.save(dishDto);

        Long dishDtoId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishDtoId);

        });
        // 保存菜品口味
        boolean saveFlavors = dfs.saveBatch(flavors);

        return saveDish && saveFlavors;
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        // 对象拷贝
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dfs.list(lqw);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public boolean updateWithFlavor(DishDto dishDto) {
        // 更新dish表
        boolean updateById = this.updateById(dishDto);
        // 清理当前菜品对应口味数据
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        Long dishDtoId = dishDto.getId();
        lqw.eq(DishFlavor::getDishId,dishDtoId);
        boolean remove = dfs.remove(lqw);
        // 添加当前菜品对应口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishDtoId);

        });
        // 保存菜品口味
        boolean saveFlavors = dfs.saveBatch(flavors);
        return updateById && remove && saveFlavors;
    }

    @Transactional
    @Override
    public boolean deleteByIdWithFlavor(Long[] ids) {

        LambdaUpdateWrapper<DishFlavor> luw = new LambdaUpdateWrapper();
        Stream<Long> idsStream = Arrays.stream(ids);
        idsStream.forEach(id -> {
            luw.eq(DishFlavor::getDishId,id);
            dfs.remove(luw);
        });
        // id删除dish对应
        boolean removeDish = this.removeByIds(Arrays.asList(ids));
        return removeDish;
    }
}




