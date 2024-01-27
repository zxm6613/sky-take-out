package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获sql语句username重复异常
     *
     * @return result<对象>
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        //获取异常信息
        String errorMessage = ex.getMessage();
        //Duplicate entry 'zhangsan' for key 'employee.idx_username'
        if (errorMessage.contains("Duplicate entry")) { //键值对重复
            //拿到username
            String[] split = errorMessage.split(" ");
            String username = split[2];
            ex.printStackTrace();
            return Result.error(username + MessageConstant.USER_ALREADY_EXIST);
        } else {
            ex.printStackTrace();
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
