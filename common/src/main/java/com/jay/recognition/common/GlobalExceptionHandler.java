package com.jay.recognition.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 标记该类为全局异常处理类
//@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理所有类型的异常
    @ExceptionHandler(Exception.class)
    public Result<String> handleAllExceptions(Exception ex) {
        // 记录异常信息，这里可以使用日志框架，如 Logback 或 SLF4J
        //ex.printStackTrace();
        return Result.fail(ex.getMessage());
    }
}
