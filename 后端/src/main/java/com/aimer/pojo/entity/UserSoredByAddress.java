package com.aimer.pojo.entity;

import com.aimer.pojo.VO.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 对user列表进行排序时候的封装类，适用与根据经纬度计算距离的排序
 */
@Data
@Builder

public class UserSoredByAddress implements Comparable<UserSoredByAddress>{
    private UserVO user;
    private BigDecimal distance;

    @Override
    public int compareTo(UserSoredByAddress userSoredByAddress) {
        return this.getDistance().compareTo(userSoredByAddress.getDistance());
    }
}
