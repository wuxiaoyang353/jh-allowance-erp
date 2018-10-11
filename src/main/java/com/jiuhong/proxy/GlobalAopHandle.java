package com.jiuhong.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 全局aop控制
 */
@Component
@Aspect
@Slf4j
public class GlobalAopHandle {

    @Pointcut("execution(public * com.jiuhong.controller.*.*(..))")
    public void webPointCut() {

    }

    @Before("webPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        //接受到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //记录下请求内容
        log.info("浏览器输入的网址=URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("执行的业务方法名=CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("业务方法获得的参数=ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "webPointCut()")
    public void doAfterReturning(Object ret) throws Throwable {
        //处理完请求，返回内容
        log.info("方法的返回值 : " + ret);
    }

    //异常通知
    @AfterThrowing("webPointCut()")
    public void throwss(JoinPoint joinPoint) {
        log.info("当前" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "方法发送异常");
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("webPointCut()")
    public void after(JoinPoint jp) {
        log.info("执行完毕");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("webPointCut()")
    public Object arround(ProceedingJoinPoint pjp) {
        log.info("方法环绕start.....");
        try {
            Object o = pjp.proceed();//如果不执行这句，会不执行切面的Before方法及controller的业务方法
            log.info("【方法环绕proceed，结果是 :" + o);
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


}
