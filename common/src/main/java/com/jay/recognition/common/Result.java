package com.jay.recognition.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Result<T> {
    private Integer code;
    private String info;
    private T data;

    public Result(Integer code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public Result(){}


    public static <T> Result<T> success() {
        return new Result<>(Constant.Response.SUCCESS.getCODE(), Constant.Response.SUCCESS.getINFO(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(Constant.Response.SUCCESS.getCODE(), Constant.Response.SUCCESS.getINFO(), data);
    }

    public static <T> Result<T> success(String info, T data) {
        return new Result<>(Constant.Response.SUCCESS.getCODE(), info, data);
    }

    public static <T> Result<T> success(Integer code, String info, T data) {
        return new Result<>(code, info, data);
    }

    public static <T> Result<T> fail() {
        return new Result<T>(Constant.Response.FAIL.getCODE(), Constant.Response.FAIL.getINFO(), null);
    }

    public static <T> Result<T> fail(String info) {
        return new Result<T>(Constant.Response.FAIL.getCODE(), info, null);
    }

    public static <T> Result<T> fail(Integer code, String info) {
        return new Result<>(code, info, null);
    }
}
