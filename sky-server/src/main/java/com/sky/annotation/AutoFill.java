package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Target(value = ElementType.METHOD) //作用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
