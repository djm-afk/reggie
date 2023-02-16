package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.reggie_take_out.common.BaseContextThreadLocal;
import com.example.reggie_take_out.pojo.Setmeal;
import com.example.reggie_take_out.pojo.ShoppingCart;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/shoppingCart")
@ResponseBody
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class shoppingCartController {

    @Autowired
    private ShoppingCartService scs;

    // 查看购物车
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getShoopingCartList(){
        LambdaQueryWrapper<ShoppingCart> lqw =new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContextThreadLocal.getCurrentId());
        lqw.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = scs.list(lqw);
        return Result.success(list);
    }

    // 清空购物车
    @DeleteMapping("/clean")
    public Result<String> clearShoopingCartList(){
        LambdaQueryWrapper<ShoppingCart> lqw =new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContextThreadLocal.getCurrentId());
        boolean remove = scs.remove(lqw);
        return remove ? Result.success("清空购物车成功") : Result.success("清空购物车失败");
    }

    // 删除购物车菜品/套餐
    @PostMapping("/sub")
    public Result<String> clearShoopingCartList(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaUpdateWrapper<ShoppingCart> luw = new LambdaUpdateWrapper<>();
        luw.eq(ShoppingCart::getUserId,BaseContextThreadLocal.getCurrentId());
        // 购物车加入菜品
        if (!Objects.isNull(dishId)){
            luw.eq(ShoppingCart::getDishId,dishId);
        }
        // 购物车加入套餐
        if (!Objects.isNull(setmealId)){
            luw.eq(ShoppingCart::getSetmealId, setmealId);
        }
        boolean remove = scs.remove(luw);
        return remove ? Result.success("删除成功") : Result.success("删除失败");
    }

    // 加入购物车
    @PostMapping("/add")
    public Result<ShoppingCart> addShoopingCartList(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据-->"+shoppingCart);
        Long currentId = BaseContextThreadLocal.getCurrentId();
        shoppingCart.setUserId(currentId);
        // 查询当前菜品或套餐是否在购物车
        LambdaQueryWrapper<ShoppingCart> lqw =new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,currentId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        // 购物车加入菜品
        if (!Objects.isNull(dishId)){
            lqw.eq(ShoppingCart::getDishId,dishId);
        }
        // 购物车加入套餐
        if (!Objects.isNull(setmealId)){
            lqw.eq(ShoppingCart::getSetmealId, setmealId);
        }
        // 查询当前菜品或套餐是否在购物车
        ShoppingCart shoppingOne = scs.getOne(lqw);

        if (!Objects.isNull(shoppingOne)){
            Integer number = shoppingOne.getNumber();
            // 存在->数量加1
            shoppingOne.setNumber(number + 1);
            scs.updateById(shoppingOne);
        }else {
            // 不存在->加入购物车
            shoppingCart.setCreateTime(LocalDateTime.now());
            scs.save(shoppingCart);
            shoppingOne = shoppingCart;
        }
        return Result.success(shoppingOne);
    }

}
