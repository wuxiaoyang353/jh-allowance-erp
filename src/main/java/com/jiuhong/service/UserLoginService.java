package com.jiuhong.service;

import com.jiuhong.entity.RoleInfo;
import com.jiuhong.entity.UserInfo;
import org.apache.shiro.authc.AuthenticationException;

import java.util.Map;

/**
 * 用户登录处理业务层
 * Created by za-wuxiaoyang on 2018/9/10.
 */
public interface UserLoginService {

    /**
     * 添加用户
     *
     * @param map
     * @return
     */
    public UserInfo addUser(Map<String, Object> map);


    /**
     * 添加角色
     *
     * @param map
     * @return
     */
    public RoleInfo addRole(Map<String, Object> map);

    /**
     * 查询用户通过用户名
     *
     * @param name
     * @return
     */
    public UserInfo findByName(String name);

    public void login(String username, String password) throws AuthenticationException;
    }
