package com.aimer.mapper;

import com.aimer.pojo.entity.Apply;
import com.aimer.pojo.response.SearchApplyListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface ApplyMapper {
    Long insertApply(Apply applyEntity);

    List<SearchApplyListResponse> queryApplyFrom(String userAccount,Date time,int beginPages,int pageSize);
    List<SearchApplyListResponse> queryApplyTo(String userAccount, Date time, int beginPages, int pageSize);

    void agreeApply(Long applyId,int is,LocalDateTime updateTime);

    Apply getApplyById(Long applyId);

    int applyIsExist(String userAccount, String acceptUserAccount);

    int selectAllUserApply(String userAccount);

}
