package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.BaseContextThreadLocal;
import com.example.reggie_take_out.common.SMSUtils;
import com.example.reggie_take_out.common.ValidateCodeUtils;
import com.example.reggie_take_out.pojo.User;
import com.example.reggie_take_out.respR.Result;
import com.example.reggie_take_out.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.Objects;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@RestController
@RequestMapping("/user")
@ResponseBody
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class userController {
    @Autowired
    private UserService us;

    // 发送手机验证码
    @PostMapping("/sendMsg")
    public Result<String> senMsg(HttpSession session, @RequestBody User user){
        // 获取手机号
        String phone = user.getPhone();

        if (!StringUtils.isEmpty(phone)){
            // 生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(" code={} "+code);
            // 调用阿里云提供的短信服务API发送短信
//            SMSUtils.sendMessage("DJM的博客","SMS_269430617",phone,code);
            // 将生成的验证码保存
            session.setAttribute(phone,code);
            return Result.success("短信发送成功");
        }
        return Result.error("短信发送失败");
    }

    // 手机验证码登录（移动端）
    @PostMapping("/login")
    public Result<User> login(HttpSession session, @RequestBody Map map){
        // 获取手机号
        String phone = (String) map.get("phone");
        // 获取验证码
        String code = (String) map.get("code");
        log.info(map.toString());

        String codeInSession = (String) session.getAttribute(phone);
        log.info(codeInSession);

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhone,phone);
        User userOne = us.getOne(lqw);
        if (!Objects.isNull(codeInSession) && codeInSession.equals(code)){
            // 判断当前手机号是否为新用户
            if (Objects.isNull(userOne)){
                // 自动注册
                userOne = new User();
                userOne.setPhone(phone);
                userOne.setStatus(1);
                boolean save = us.save(userOne);
            }
            session.removeAttribute(phone);
            session.setAttribute("user",userOne);
            return Result.success(userOne);
        }else {
            return Result.error("登录失败！");
        }

    }

}
