package com.jiuhong.service.impl;

import com.jiuhong.dao.RoleInfoDao;
import com.jiuhong.dao.UserInfoDao;
import com.jiuhong.entity.PermissionInfo;
import com.jiuhong.entity.RoleInfo;
import com.jiuhong.entity.UserInfo;
import com.jiuhong.service.UserLoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by za-wuxiaoyang on 2018/9/10.
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RoleInfoDao roleInfoDao;

    /**
     * 添加用户
     *
     * @param map
     * @return
     */
    @Override
    public UserInfo addUser(Map<String, Object> map) {
        //用户名
        String username = (String) map.get("username");
        //密码
        String password = (String) map.get("password");

        UserInfo userInfo = new UserInfo();

        userInfo.setUserName(username);
        userInfo.setPassword(password);
        userInfoDao.save(userInfo);
        return userInfo;
    }

    /**
     * 添加角色
     *
     * @param map
     * @return
     */
    @Override
    public RoleInfo addRole(Map<String, Object> map) {
        Long userId = Long.valueOf((String) map.get("userId"));

//        UserInfo userInfo = userInfoDao.findOne(userId);
        UserInfo userInfo = new UserInfo();
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleName(map.get("roleName").toString());
        roleInfo.setUserInfo(userInfo);

        PermissionInfo createPermission = new PermissionInfo();
        createPermission.setPermission("create");
        createPermission.setRoleInfo(roleInfo);

        PermissionInfo updatePermission = new PermissionInfo();
        updatePermission.setPermission("update");
        updatePermission.setRoleInfo(roleInfo);
        List<PermissionInfo> permissions = new ArrayList<PermissionInfo>();
        permissions.add(createPermission);
        permissions.add(updatePermission);
        roleInfo.setPermissions(permissions);
        roleInfoDao.save(roleInfo);
        return roleInfo;
    }

    /**
     * 查询用户通过用户名
     *
     * @param name
     * @return
     */
    @Override
    public UserInfo findByName(String name) {
        return userInfoDao.selectUserInfoByName(name);
    }

    @Override
    public void login(String username, String password) throws AuthenticationException {

    }
}
