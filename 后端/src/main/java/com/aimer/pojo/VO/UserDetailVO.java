package com.aimer.pojo.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Year;
@Data
public class UserDetailVO {

    /**
     * 用户昵称
     */
    private String username;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 标签列表 json
     */
    private String tags;
    /**
     * 地理名
     */
    private String address;
    /**
     * 出生年龄
     */
    private Year age;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 自我介绍
     */
    private String introduction;
    /**
     * 问题
     */
    private String problem;
}
