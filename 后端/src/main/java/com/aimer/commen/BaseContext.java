package com.aimer.commen;

public class BaseContext {
    protected static final ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static Long getId(){
        return threadLocal.get();
    }
    public static void setId(Long id){
        threadLocal.set(id);
    }
    public static void remove(){
        threadLocal.remove();
    }
}
