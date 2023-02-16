package com.example.reggie_take_out.service;

import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 32580
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-02-07 11:10:03
*/
public interface SetmealService extends IService<Setmeal> {

    @Transactional
    // 新增套餐
    boolean saveWithFlavor(SetmealDto setmealDto);

    @Transactional
    // id查套餐
    SetmealDto getByIdWithFlavor(Long id);

    boolean updateWithFlavor(SetmealDto setmealDto);

    boolean deleteByIdWithFlavor(Long[] ids);
}
