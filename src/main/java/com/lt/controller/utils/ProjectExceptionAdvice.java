package com.lt.controller.utils;

import com.lt.exception.BusinessException;
import com.lt.exception.SystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//用于定义全局异常处理和全局数据绑定规则的类
@RestControllerAdvice
public class ProjectExceptionAdvice {
    //能够拦截异常信息——>
    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex){
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        ex.printStackTrace();//终端输出异常信息
        return new Result(ex.getCode(),null,ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex){
        ex.printStackTrace();//终端输出异常信息
        return new Result(ex.getCode(),null,ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result doOtherException(Exception ex){
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        ex.printStackTrace();//终端输出异常信息
        return new Result(Code.SYSTEM_UNKNOW_ERR,"系统繁忙，请稍后再试！",ex.getMessage());
    }
}
