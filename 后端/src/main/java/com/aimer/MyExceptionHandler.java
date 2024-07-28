package com.aimer;

import com.aimer.commen.Result;
import com.aimer.commen.myException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class MyExceptionHandler {
    @ExceptionHandler(myException.class)
    public Result<Object> exceptionHandler(myException e){
        return Result.error(e.getMessage());
    }
}
