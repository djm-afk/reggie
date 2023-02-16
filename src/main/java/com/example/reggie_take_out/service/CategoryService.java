package com.example.reggie_take_out.service;

import com.example.reggie_take_out.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 32580
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2023-02-07 11:10:03
*/
public interface CategoryService extends IService<Category> {

    boolean remove(Long categoryId);

}
