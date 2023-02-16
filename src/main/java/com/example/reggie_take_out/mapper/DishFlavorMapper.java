package com.example.reggie_take_out.mapper;

import com.example.reggie_take_out.pojo.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 32580
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
* @createDate 2023-02-07 11:10:03
* @Entity com.example.reggie_take_out.pojo.DishFlavor
*/
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




