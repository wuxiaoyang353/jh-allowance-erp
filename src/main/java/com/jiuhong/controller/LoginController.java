package com.jiuhong.controller;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by za-wuxiaoyang on 2018/9/11.
 */
@RestController
public class LoginController {

    @Autowired
    private ResultMap resultMap;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultMap login(String userName, String password) {
        Subject subject = SecurityUtils.getSubject();

        //在认证提交前准备token令牌
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);

        //登录
        subject.login(token);

        //根据权限，指定返回数据
        return null;
    }
}
