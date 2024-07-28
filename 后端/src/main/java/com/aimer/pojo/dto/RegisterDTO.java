package com.aimer.pojo.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;


    /**
     * 密码
     */
    private String userPassword;
    /**
     * 校验密码
     */

    private String checkPassword;
}
