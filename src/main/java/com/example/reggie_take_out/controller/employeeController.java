package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.pojo.Employee;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@ResponseBody
@RequestMapping("/employee")
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class employeeController {

    @Autowired
    private EmployeeService es;

    // 登录
    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = es.getOne(lqw);
        if (Objects.isNull(emp)){
            return Result.error("登陆失败，用户名或密码错误1");
        }
        if (!password.equals(emp.getPassword())){
            return Result.error("登陆失败，用户名或密码错误2");
        }
        if (emp.getStatus() == 0 ){
            return Result.error("登陆失败，此账户被禁用");
        }
        request.getSession().setAttribute("employee",emp);
        return Result.success(emp);
    }

    // 登出
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    // 新增员工
    @PostMapping
    public Result addUser(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        boolean save = es.save(employee);
        return save ? Result.success("新增成功") : Result.success("新增失败");
    }

    // 分页查询员工
    @GetMapping("/page")
    public Result<Page> selectByPage(@PathParam("page") Integer page, @PathParam("pageSize") Integer pageSize, @PathParam("name")String name){
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        lqw.like(!Strings.isEmpty(name),Employee::getName,name);
        lqw.orderByDesc(Employee::getCreateTime);
        Page page_ = es.page(new Page(page, pageSize), lqw);
        long pages = page_.getPages();
        System.out.println(pages);
        if (pages < page){
            page_ = es.page(new Page(pages, pageSize), lqw);
        }
        return Result.success(page_);
    }

    // 禁用/启用员工 修改也是
    @PutMapping
    public Result employeeStatus(@RequestBody Employee employee){
        employee.setStatus(employee.getStatus());
        boolean b = es.updateById(employee);
        return b ? Result.success("修改状态成功！") : Result.error("修改状态失败！");
    }

    // id查询员工
    @GetMapping("/{id}")
    public Result<Employee> selectByPage(@PathVariable Long id){
        Employee employee = es.getById(id);
        return Objects.isNull(employee) ? Result.error("查询失败，没有查询到信息") : Result.success(employee);
    }
}
