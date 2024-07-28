package com.aimer.commen.validator;

import com.aimer.commen.myException;
import com.aimer.constant.ExceptionMessage;

import java.lang.reflect.Field;

/**
 * 检验类极其属性是否为空是否为空的工具类
 * @param <T>
 */
public class NullValidator<T> {

    public static <T> void validate(T object) throws myException {
        if(object==null){
            throw new myException(ExceptionMessage.EXIST_NULL);
        }
        Class<?> objClass = object.getClass();
        Field[] fields = objClass.getDeclaredFields();
        for(Field field :fields){
            field.setAccessible(true);
            try {
                // 获取成员属性的类型
                Class<?> fieldType = field.getType();
                Object fieldValue = field.get(object);
                if (fieldType.isAssignableFrom(Class.class)) {
                    // 如果是类为空，则抛出错误
                    if(fieldValue==null){
                        throw new myException(ExceptionMessage.EXIST_NULL);
                    }
                    if(fieldType.isAssignableFrom(String.class)&& ((String) fieldValue).matches("\s*")){
                        throw new myException(ExceptionMessage.EXIST_NULL_CHARACTER);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
