package com.example.reggie_take_out.service;

import com.example.reggie_take_out.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 32580
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-02-07 11:10:03
*/
public interface OrdersService extends IService<Orders> {

    boolean submit(Orders orders);
}
