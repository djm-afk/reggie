package com.example.reggie_take_out.controller;

import com.example.reggie_take_out.common.BaseContextThreadLocal;
import com.example.reggie_take_out.pojo.Orders;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
