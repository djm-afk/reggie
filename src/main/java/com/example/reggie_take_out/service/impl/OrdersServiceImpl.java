package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.BaseContextThreadLocal;
import com.example.reggie_take_out.controller.exceptionController.exception.ShoppingCartEx;
import com.example.reggie_take_out.pojo.*;
import com.example.reggie_take_out.service.*;
import com.example.reggie_take_out.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
* @author 32580
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Autowired
    private ShoppingCartService scs;
    @Autowired
    private AddressBookService abs;
    @Autowired
    private UserService us;
    @Autowired
    private OrderDetailService ods;

    // 用户下单
    @Override
    @Transactional
    public boolean submit(Orders orders) {
        Long currentId = BaseContextThreadLocal.getCurrentId();
        // 查询当前用户购物车
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> shoppingCarts = scs.list(lqw); // 购物车信息
        if (Objects.isNull(shoppingCarts)){
            throw new ShoppingCartEx("购物车为空，无法下单");
        }
        // 价格（原子操作，保证线程安全）
        AtomicInteger amount = new AtomicInteger(0);
        long orderId = IdWorker.getId(); //订单号
        List<OrderDetail> orderDetails = shoppingCarts.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        // 用户数据
        User user = us.getById(currentId);
        // 地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = abs.getById(addressBookId);
        if (Objects.isNull(shoppingCarts)){
            throw new ShoppingCartEx("用户地址有误，无法下单");
        }
        // 订单表插入数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(currentId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
        // 订单详细表插入（单/多）条数据
        boolean saveBatchOD = ods.saveBatch(orderDetails);
        // 清空购物车
        LambdaUpdateWrapper<ShoppingCart> luw = new LambdaUpdateWrapper<>();
        luw.eq(ShoppingCart::getUserId,currentId);
        scs.remove(luw);

        return saveBatchOD;
    }
}




