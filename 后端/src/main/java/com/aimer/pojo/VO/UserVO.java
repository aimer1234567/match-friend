package com.aimer.pojo.VO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Date;

@Data
public class UserVO implements Serializable {

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
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 维度
     */
    private BigDecimal dimension;

    private Year age;

    private static final long serialVersionUID = 1L;
}

