package com.jiuhong.proxy;

import com.jiuhong.util.RedisConstantCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 登录拦截校验
 * Created by za-wuxiaoyang on 2018/9/11.
 */
@Component
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("登录拦截器============");
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        try {
            //检验游湖是否已经登录，如果登录过，将之前用户踢掉并更新缓存信息
            Subject subject = SecurityUtils.getSubject();
            Serializable token = subject.getSession().getId();
            if (null == token) {
                log.info("token is null");
                return false;
            }
            String jhUserTokenKey = RedisConstantCommonUtil.getKey(RedisConstantCommonUtil.JH_USRE_TOKEN, token.toString());


            //获取用户id
            String userId = ops.get(jhUserTokenKey);
            String jhUserIdKey = RedisConstantCommonUtil.getKey(RedisConstantCommonUtil.JH_USRE_ID, userId);
            if (StringUtils.isNotBlank(userId)) {
                String tokenInfo = ops.get(jhUserIdKey);
                if (!token.equals(tokenInfo)) {
                    //重定向到登录页
                    redirect(request,response);
                    return false;
                } else {
                    Long expire = stringRedisTemplate.getExpire(RedisConstantCommonUtil.getKey(RedisConstantCommonUtil.JH_USRE_TOKEN, token.toString()));
                    //过期时间小于1分钟，更新token
                    if (expire < 1 * 60 * 1000) {
                        stringRedisTemplate.expire(jhUserTokenKey, 60 * 30, TimeUnit.SECONDS);
                    }
                }
            } else {
                redirect(request,response);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object
            handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object
            handler, @Nullable Exception ex) throws Exception {

    }

    //对于请求是ajax请求重定向问题的处理方法
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取当前请求路径
        String basePath = request.getScheme() +
                "://" + request.getServerName() +
                ":" + request.getServerPort() +
                request.getContextPath();
        response.getOutputStream().write("账号在别处登录。".getBytes("UTF-8"));
        //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理 否则直接重定向就可以了
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            //告诉ajax我重定向的路径
            response.setHeader("CONTENTPATH", basePath+"/login");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            response.sendRedirect(basePath + "/login");
        }
    }
}
