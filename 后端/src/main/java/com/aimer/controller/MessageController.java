package com.aimer.controller;

import com.aimer.commen.Result;
import com.aimer.pojo.VO.CommunicationVO;
import com.aimer.pojo.request.ApplyRequest;
import com.aimer.pojo.response.SearchApplyListResponse;
import com.aimer.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    /**
     * 发送验证消息
     * @param apply
     * @return
     */
    @PostMapping("/send/apply")
    public Result<Object> sentApply(@RequestBody ApplyRequest apply){
        messageService.sentApply(apply);
        return Result.success();
    }
    /**
     * 查询我发出的申请列表
     */
    @GetMapping("/query/applyTo")
    public Result<List<SearchApplyListResponse>> queryApplyTo(@RequestParam int pages,
                                                              @RequestParam int pageSize,
                                                              @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") @RequestParam Date time){
        List<SearchApplyListResponse> applies = messageService.queryApplyTo(pages,pageSize,time);
        return Result.success(applies);
    }
    /**
     * 查询我收到的申请列表
     */
    @GetMapping("/query/applyFrom")
    public Result<List<SearchApplyListResponse>> queryApplyFrom(@RequestParam int pages,
                                                                @RequestParam int pageSize,
                                                                @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") @RequestParam Date time){
        List<SearchApplyListResponse> applies = messageService.queryApplyFrom(pages,pageSize,time);
        return Result.success(applies);
    }
    /**
     * 获取联系方式
     * @return
     */
    @GetMapping("/send")
    public Result<CommunicationVO> sent(@RequestParam String acceptUserAccount){
        CommunicationVO sent = messageService.sent(acceptUserAccount);
        return Result.success(sent);
    }
    /**
     * 同意或拒绝申请
     */
    @GetMapping("/agree")
    public Result<Object> agreeApply(@RequestParam Long applyId,@RequestParam Integer is){
        messageService.agreeApply(applyId,is);
        return Result.success();
    }
    /**
     * 同意后，申请获取联系方式
     */
    @GetMapping("/sendByApply")
    public Result<CommunicationVO> sentByApply(@RequestParam String userAccount,@RequestParam long applyId){
        CommunicationVO communicationVO = messageService.sentByApply(userAccount, applyId);
        return Result.success(communicationVO);
    }
}
