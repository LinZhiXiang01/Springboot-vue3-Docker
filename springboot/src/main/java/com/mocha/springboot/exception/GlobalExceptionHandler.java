package com.mocha.springboot.exception;

import com.mocha.springboot.common.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.mocha.springboot.controller")
public class GlobalExceptionHandler {

    //捕获Exception类型的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody // 返回json串
    public ResultCode error(Exception e) {
        e.printStackTrace();
        return ResultCode.error();
    }

    //捕获Exception类型的异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody // 返回json串
    public ResultCode error(CustomException e) {
        e.printStackTrace();
        return ResultCode.error(e.getMsg(),e.getCode());
    }
}

