package com.example.reggie_take_out.dto;


import com.example.reggie_take_out.pojo.Setmeal;
import com.example.reggie_take_out.pojo.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes = new ArrayList<>();

    private String CategoryName;
}
