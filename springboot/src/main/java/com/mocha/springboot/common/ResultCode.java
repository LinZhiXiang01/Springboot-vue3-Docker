package com.mocha.springboot.common;

//统一后端返回的数据类型
public class ResultCode {

    private int code;
    private String msg;
    private Object data;

    public static ResultCode success(){
        ResultCode result = new ResultCode();
        result.setCode(200);
        result.setMsg("success");

        return result;
    }

    public static ResultCode error(){
        ResultCode result = new ResultCode();
        result.setCode(500);
        result.setMsg("系统错误");
        return result;
    }

    public static ResultCode error(String msg, int code){
        ResultCode result = new ResultCode();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static ResultCode success(Object data){
        ResultCode result = success();
        result.setData(data);
        return result;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data= data;
    }
}