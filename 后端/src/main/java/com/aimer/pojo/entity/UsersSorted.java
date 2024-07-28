package com.aimer.pojo.entity;

import com.aimer.pojo.VO.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对user列表进行排序时候的封装类
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersSorted  implements Comparable<UsersSorted>{
    private UserVO user;
    private Integer num;

    @Override
    public int compareTo(UsersSorted usersSorted) {
        return -this.getNum()+usersSorted.getNum();
    }
}
