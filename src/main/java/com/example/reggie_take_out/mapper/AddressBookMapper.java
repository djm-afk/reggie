package com.example.reggie_take_out.mapper;

import com.example.reggie_take_out.pojo.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 32580
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2023-02-07 11:10:03
* @Entity com.example.reggie_take_out.pojo.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




