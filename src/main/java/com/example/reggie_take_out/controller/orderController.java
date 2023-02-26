package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.BaseContextThreadLocal;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.pojo.Category;
import com.example.reggie_take_out.pojo.Dish;
import com.example.reggie_take_out.pojo.Orders;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.OrdersService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/order")
@ResponseBody
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class orderController {

    @Autowired
    private OrdersService os;

    // 购物车结算(用户下单)
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        log.info(orders.toString());
        boolean submit = os.submit(orders);
        return submit ? Result.success("下单成功") : Result.success("下单失败");
    }

    // 查看订单
    @GetMapping("/page")
    public Result<Page> selectByPage(@PathParam("page") Integer page, @PathParam("pageSize") Integer pageSize,
                                     @PathParam("beginTime") String beginTime, @PathParam("endTime")String endTime,
                                     @PathParam("number") Long number){
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper();
        lqw.gt(!Objects.isNull(beginTime),Orders::getOrderTime,beginTime);
        lqw.lt(!Objects.isNull(endTime),Orders::getOrderTime,endTime);
        lqw.eq(!Objects.isNull(number), Orders::getNumber,number);
        lqw.orderByAsc(Orders::getOrderTime);
        Page page_ = os.page(new Page(page, pageSize), lqw);
        long pages = page_.getPages();
        if (pages < page){
            page_ = os.page(new Page(pages, pageSize), lqw);
        }
        return Result.success(page_);
    }

    // 派送
    @PutMapping()
    public Result<String> delivery(@RequestBody Orders orders){
        log.info(orders.toString());
        boolean update = os.updateById(orders);
        return update ? Result.success("派送成功") : Result.success("派送失败");
    }
}
