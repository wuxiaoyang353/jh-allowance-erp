package com.jiuhong.proxy;

import com.alibaba.fastjson.JSONObject;
import com.jiuhong.entity.PermissionInfo;
import com.jiuhong.entity.RoleInfo;
import com.jiuhong.entity.UserInfo;
import com.jiuhong.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * shiro权限控制
 * Created by za-wuxiaoyang on 2018/9/10.
 */
@Slf4j
public class ShiroRealmProxy extends AuthorizingRealm {

    @Autowired
    private UserLoginService userLoginService;


    /**
     * 角色权限和对应权限添加
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录用户名
        String name = (String) principals.getPrimaryPrincipal();

        //查询用户名称
        UserInfo userInfo = new UserInfo();

        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (RoleInfo roleInfo : userInfo.getRoles()) {
            //添加角色
            simpleAuthorizationInfo.addRole(roleInfo.getRoleName());

            for (PermissionInfo permissionInfo : roleInfo.getPermissions()) {
                //添加权限
                simpleAuthorizationInfo.addStringPermission(permissionInfo.getPermission());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (null == authenticationToken) {
            log.info("当前authenticationToken为空");
            return null;
        }

        //获取用户信息
        String name = (String) authenticationToken.getPrincipal();
        log.info("当前登录的用户为：{}", name);

        UsernamePasswordToken passwordToken = (UsernamePasswordToken) authenticationToken;
        UserInfo userInfo = userLoginService.findByName(name);

        if (null == userInfo) {
            log.info("当前系统不存在此用户，请创建用户后再操作");
            throw new AccountException("不存在此用户");
        }
        String password = userInfo.getPassword();
        if (StringUtils.isBlank(password)) {
            throw new AccountException("用户名不正确");
        }
        if (!StringUtils.equals(password, new String(passwordToken.getPassword()))) {
            throw new AccountException("密码不正确");
        }
        log.info("查询出的用户信息为：{}", JSONObject.toJSONString(userInfo));
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, userInfo.getPassword(), ByteSource.Util.bytes(userInfo.getCredentialsSalt()), getName());
        return simpleAuthenticationInfo;
    }
}
