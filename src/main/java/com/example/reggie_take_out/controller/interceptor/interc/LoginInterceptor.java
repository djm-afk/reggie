package com.example.reggie_take_out.controller.interceptor.interc;

import com.alibaba.fastjson.JSON;
import com.example.reggie_take_out.common.BaseContextThreadLocal;
import com.example.reggie_take_out.pojo.Employee;
import com.example.reggie_take_out.pojo.User;
import com.example.reggie_take_out.respR.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/*
*   拦截器 实现HandlerInterceptor接口
* */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器拦截请求 "+request.getRequestURL());
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        User user = (User) request.getSession().getAttribute("user");
        log.info("当前线程--->"+Thread.currentThread().getId());
        if (!Objects.isNull(employee)){
            BaseContextThreadLocal.setCurrentId(employee.getId());
            return true;
        }else if (!Objects.isNull(user)){
            BaseContextThreadLocal.setCurrentId(user.getId());
            return true;
        }else {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("拦截器postHandle... ");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("拦截器afterCompletion... ");
    }
}
