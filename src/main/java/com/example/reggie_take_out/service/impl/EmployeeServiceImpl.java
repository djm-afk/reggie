package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.pojo.Employee;
import com.example.reggie_take_out.service.EmployeeService;
import com.example.reggie_take_out.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 32580
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




