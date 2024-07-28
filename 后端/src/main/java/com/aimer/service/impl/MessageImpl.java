package com.aimer.service.impl;

import com.aimer.commen.BaseContext;
import com.aimer.commen.myException;
import com.aimer.constant.ExceptionMessage;
import com.aimer.mapper.ApplyMapper;
import com.aimer.mapper.UserMapper;
import com.aimer.pojo.VO.CommunicationVO;
import com.aimer.pojo.entity.Apply;
import com.aimer.pojo.entity.User;
import com.aimer.pojo.request.ApplyRequest;
import com.aimer.pojo.response.SearchApplyListResponse;
import com.aimer.service.MessageService;
import com.aimer.utils.SseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MessageImpl implements MessageService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    SseClient sseClient;
    @Autowired
    ApplyMapper applyMapper;
    /**
     * 一天最多发送多少个申请
     */
    private static final int MAX_DAY_APPLY=10;
    /**
     * 发送申请
     * @param apply  TODO 不能重复对同一个人发申请 一天最多发送10个申请
     */
    @Override
    public void sentApply(ApplyRequest apply) {
        Long id = BaseContext.getId();
        User userInformation = userMapper.getUserInformation(id);
        String userAccount = userInformation.getUserAccount();
        //查询是否对他发送过好友申请
        if(applyMapper.applyIsExist(userAccount,apply.getAcceptUserAccount())>=1){
            throw new myException(ExceptionMessage.APPLY_EXIST);
        }
        if(applyMapper.applyIsExist(apply.getAcceptUserAccount(),userAccount)>=1){
            throw new myException(ExceptionMessage.APPLY_EXIST);
        }
        //查询是当日发送的申请是否超过总数
        if(applyMapper.selectAllUserApply(userAccount)>=MAX_DAY_APPLY){
            throw new myException(ExceptionMessage.EXCEED_MAX_DAY_APPLY);
        }
        Apply applyEntity = new Apply();
        BeanUtils.copyProperties(apply,applyEntity);
        applyEntity.setApplyUserAccount(userAccount);
        applyEntity.setIntroduction(userInformation.getIntroduction());
        applyEntity.setCreateTime(LocalDateTime.now());
        applyEntity.setUpdateTime(LocalDateTime.now());
        applyEntity.setIsAgree(2);
        //存储申请
        applyMapper.insertApply(applyEntity);
        //消息id是发送者账号加时间戳
        String messageId=applyEntity.getApplyUserAccount()+LocalDateTime.now();
        String message="你有新的好友申请";
        //发送消息
        sseClient.sendMessage(applyEntity.getAcceptUserAccount(),messageId,message);
    }

    /**
     * 获取联系方式
     * @param userAccount
     * @return
     */

    @Override
    public CommunicationVO sent(String userAccount) {
        User user = userMapper.selectUserByAccount(userAccount);
        if(!user.getProblem().equals("无")){
            throw new myException(ExceptionMessage.ERROR);
        }
        CommunicationVO communicationVO = new CommunicationVO();
        communicationVO.setPhone(user.getPhone());
        communicationVO.setWchat(user.getWchat());
        return communicationVO;
    }

    /**
     * 查询我发给别人的申请
     * @return
     */

    @Override
    public List<SearchApplyListResponse> queryApplyTo(int pages, int pageSize, Date time) {
        Long id = BaseContext.getId();
        User userInformation = userMapper.getUserInformation(id);
        int beginPages=pages*pageSize;
        return applyMapper.queryApplyTo(userInformation.getUserAccount(),time,beginPages,pageSize);
    }
    /**
     * 查询别人发给我的申请
     * @return
     */
    @Override
    public List<SearchApplyListResponse> queryApplyFrom(int pages,int pageSize,Date time) {
        Long id = BaseContext.getId();
        User userInformation = userMapper.getUserInformation(id);
        int beginPages=pages*pageSize;
       return applyMapper.queryApplyFrom(userInformation.getUserAccount(),time,beginPages,pageSize);
    }
    /**
     * 同意申请
     */
    @Override
    public void agreeApply(Long applyId,int is){
        Long id = BaseContext.getId();
        User userInformation = userMapper.getUserInformation(id);
        Apply apply = applyMapper.getApplyById(applyId);
        //检验权限
        if(!userInformation.getUserAccount().equals(apply.getAcceptUserAccount())){
            throw new myException(ExceptionMessage.ERROR);
        }
        String username=userInformation.getUsername();
        //消息id是发送者账号加时间戳
        String messageId=apply.getApplyUserAccount()+LocalDateTime.now();
        if(is==0){
            sseClient.sendMessage(apply.getApplyUserAccount(),messageId,username+":申请拒绝");
        }else{
            sseClient.sendMessage(apply.getApplyUserAccount(),messageId,username+":申请同意");
        }
        applyMapper.agreeApply(applyId,is,LocalDateTime.now());
    }

    /**
     * 在获取用户消息前进行检验
     * @param userAccount
     * @param applyId
     * @return
     */

    @Override
    public CommunicationVO sentByApply(String userAccount,long applyId) {
        Long id = BaseContext.getId();
        User userInformation = userMapper.getUserInformation(id);
        String myUserAccount = userInformation.getUserAccount();
        Apply apply = applyMapper.getApplyById(applyId);
        if(!Objects.equals(apply.getApplyUserAccount(), userAccount)
                && !Objects.equals(apply.getAcceptUserAccount(), userAccount)){
            throw new myException(ExceptionMessage.ERROR);
        }
        if(!Objects.equals(apply.getApplyUserAccount(), myUserAccount)
                && !Objects.equals(apply.getAcceptUserAccount(), myUserAccount)){
            throw new myException(ExceptionMessage.ERROR);
        }
        if(apply.getIsAgree()!=1){
            throw new myException(ExceptionMessage.ERROR);
        }
        User user = userMapper.selectUserByAccount(userAccount);
        CommunicationVO communicationVO = new CommunicationVO();
        communicationVO.setPhone(user.getPhone());
        communicationVO.setWchat(user.getWchat());
        return communicationVO;
    }
}
