package com.aimer.mapper;

import com.aimer.pojo.VO.UserDetailVO;
import com.aimer.pojo.VO.UserVO;
import com.aimer.pojo.dto.RegisterDTO;
import com.aimer.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {
    Integer selectByAccount(String userAccount);

    void insertUser(RegisterDTO registerDTO);

    User selectUserByAccount(String userAccount);

    List<UserVO> selectList(int index,int pageSize,long id);

    User getUserInformation(Long id);

    void updateUserInformation(User user);

    String getUserTags(long id);

    void updateAvatarUrl(String fileUrl,Long id);

    UserDetailVO getUserDetailVOByUserAccount(String userAccount);

    Integer selectUserConut(long id);
}
