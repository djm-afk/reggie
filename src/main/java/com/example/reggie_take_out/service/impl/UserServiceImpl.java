package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.pojo.User;
import com.example.reggie_take_out.service.UserService;
import com.example.reggie_take_out.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 32580
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




