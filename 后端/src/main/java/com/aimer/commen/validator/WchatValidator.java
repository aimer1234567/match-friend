package com.aimer.commen.validator;

import com.aimer.commen.myException;
import com.aimer.constant.ExceptionMessage;

public class WchatValidator {
    public static void validate(String object) throws myException {
        if(!object.matches("^[a-zA-Z0-9_]{1,20}$")){
            throw new myException(ExceptionMessage.WCHAT_NO_Standard);
        }
    }
}
