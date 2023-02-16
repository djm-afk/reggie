package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.pojo.User;
import com.example.reggie_take_out.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Reggie/User")
@ResponseBody
@CrossOrigin(origins = "*",maxAge = 3600)
public class testController {

    @Autowired
    private UserService us;

    @GetMapping("/{id}")
    public User testOne(@PathVariable long id){
        return us.getById(id);
    }

    @GetMapping()
    public List<Map<String, Object>> testMap(){
        QueryWrapper qw = new QueryWrapper<>();
        List<Map<String, Object>> maps = us.listMaps(qw);
        return maps;
    }

    @GetMapping("/{currentPage}/{pageSize}")
    public IPage<User> testPage(@PathVariable Integer currentPage, @PathVariable Integer pageSize){
        Page page = us.page(new Page(currentPage, pageSize), null);
        long pages = page.getPages();
        System.out.println(pages);
        if (pages < currentPage){
            page = us.page(new Page(pages, pageSize), null);
        }
        return page;
    }

    @PostMapping
    public Boolean save(@RequestBody User user){
        return us.save(user);
    }

    @PutMapping
    public Boolean update(@RequestBody User user){
        return us.updateById(user);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable long id){
        return us.removeById(id);
    }

}
