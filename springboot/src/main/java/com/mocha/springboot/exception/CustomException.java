package com.mocha.springboot.exception;

public class CustomException extends RuntimeException {
    private int code;
    private String msg;

    public CustomException(String message,int code) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
