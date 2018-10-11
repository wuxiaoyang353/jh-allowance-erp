package com.jiuhong.util;

import com.jiuhong.entity.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * Created by za-wuxiaoyang on 2018/9/12.
 */
public class ShiroUtils {

    @Autowired
    private static SessionDAO sessionDAO;


    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static UserInfo getUser() {
        Object object = getSubjct().getPrincipal();
        return (UserInfo)object;
    }
    public static String getUserId() {
        return getUser().getUserId();
    }
    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
}
