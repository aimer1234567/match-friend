package com.aimer.commen.validator;

import com.aimer.commen.myException;
import com.aimer.constant.ExceptionMessage;

public class PhoneValidator {
    public  static void validate(String object) throws myException {
        if(!object.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")){
            throw  new myException(ExceptionMessage.PHONE_ERROR);
        }
    }
}
