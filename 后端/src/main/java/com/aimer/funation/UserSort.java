package com.aimer.funation;

import com.aimer.pojo.VO.UserVO;

import java.util.List;
@FunctionalInterface
public interface UserSort {
    List<UserVO> sort(List<UserVO> userVOList);
}
