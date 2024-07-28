package com.aimer.commen.validator;

import com.aimer.commen.myException;
import com.aimer.constant.ExceptionMessage;

public class UsernameValidator {
    public static  void validate(String object) throws myException {
        if(!object.matches("^(?!\\s)(?!.*\\s).{1,10}$")){
            System.out.println(object.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$\n"));
            throw new myException(ExceptionMessage.USERNAME_NO_Standard);
        }
    }
}
