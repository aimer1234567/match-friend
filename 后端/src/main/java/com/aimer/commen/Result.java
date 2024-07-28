package com.aimer.commen;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private T data;
    private String msg;
    private int code=1; //1为响应正确
    public static <T> Result<T> success(T data){
        Result<T> tResult = new Result<>();
        tResult.data=data;
        tResult.code=1;
        return tResult;
    }
    public static Result<Object> success(){
        return new Result<>();
    }
    public static Result<Object> error(String msg){
        Result<Object> tResult = new Result<>();
        tResult.code=0;
        tResult.msg=msg;
        return tResult;
    }
}
