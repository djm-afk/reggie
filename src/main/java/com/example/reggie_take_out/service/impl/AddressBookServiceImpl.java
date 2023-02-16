package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.pojo.AddressBook;
import com.example.reggie_take_out.service.AddressBookService;
import com.example.reggie_take_out.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 32580
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-02-07 11:10:03
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




