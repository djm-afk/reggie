package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.pojo.ShoppingCart;
import com.example.reggie_take_out.service.ShoppingCartService;
import com.example.reggie_take_out.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 32580
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




