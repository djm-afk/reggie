package com.example.reggie_take_out.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 订单号
     */
    @TableField(value = "number")
    private String number;

    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 下单用户
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 地址id
     */
    @TableField(value = "address_book_id")
    private Long addressBookId;

    /**
     * 下单时间
     */
    @TableField(value = "order_time")
    private LocalDateTime orderTime;

    /**
     * 结账时间
     */
    @TableField(value = "checkout_time")
    private LocalDateTime checkoutTime;

    /**
     * 支付方式 1微信,2支付宝
     */
    @TableField(value = "pay_method")
    private Integer payMethod;

    /**
     * 实收金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 
     */
    @TableField(value = "address")
    private String address;

    /**
     * 
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 
     */
    @TableField(value = "consignee")
    private String consignee;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}