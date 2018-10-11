package com.jiuhong.dao;

import com.jiuhong.entity.UserInfo;

/**
 * Created by za-wuxiaoyang on 2018/9/10.
 */
public interface UserInfoDao extends BaseRepository<UserInfo, Long> {


    /**
     * 根据用户名查询用户信息
     * @param name
     * @return
     */
    UserInfo selectUserInfoByName(String name);

}
