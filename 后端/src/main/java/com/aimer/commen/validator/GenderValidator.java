package com.aimer.commen.validator;

import com.aimer.commen.myException;
import com.aimer.constant.ExceptionMessage;

public class GenderValidator {
    public static void validate(Integer gender){
        if(!(gender == 1 || gender == 0)){
            throw new myException(ExceptionMessage.PARAMS_ERROR);
        }
    }
}
