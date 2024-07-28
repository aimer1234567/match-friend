package com.aimer.service;

import com.aimer.pojo.VO.CommunicationVO;
import com.aimer.pojo.request.ApplyRequest;
import com.aimer.pojo.response.SearchApplyListResponse;

import java.util.Date;
import java.util.List;

public interface MessageService {
    /**
     * 发送申请
     * @param apply
     */
    void sentApply(ApplyRequest apply);

    /**
     * 直接获取联系方式
     * @param userAccount
     * @return
     */

    CommunicationVO sent(String userAccount);

    List<SearchApplyListResponse> queryApplyTo(int pages,int pageSize, Date time);

    List<SearchApplyListResponse> queryApplyFrom(int pages, int pageSize, Date time);

    void agreeApply(Long id,int is);

    CommunicationVO sentByApply(String userAccount,long applyId);
}
