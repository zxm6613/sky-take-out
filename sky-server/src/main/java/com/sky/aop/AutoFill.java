package com.sky.aop;

import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * AOP自动填充
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Aspect
@Component
public class AutoFill {

    //指定切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {
    }

    /**
     * 增强
     *
     * @param joinPoint 加入点
     */
    @Before("autoFillPointcut()")
    public void addAutoFill(JoinPoint joinPoint) {
        //获取拦截的方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取到这个方法对象
        Method method = signature.getMethod();
        com.sky.annotation.AutoFill autoFill = method.getAnnotation(com.sky.annotation.AutoFill.class);
        //拿到方法中的参数
        Object[] args = joinPoint.getArgs();
        Object entity = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        if (autoFill.value().equals(OperationType.INSERT)) {
            //插入操作
            /*
            通过调用的方法很容易理解：
            首先拿到对象后，获取这个对象的类型对象，在这个类对象里通过方法名以及指定这个方法参数的类型拿到方法对象
            然后把对象和值传进去
            * */
            try {
                entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod("setCreateUser", Long.class).invoke(entity, currentId);
                entity.getClass().getDeclaredMethod("setUpdateUser", Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (autoFill.value().equals(OperationType.UPDATE)) {
            //修改操作
            try {
                entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod("setUpdateUser", Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
