package com.aimer.pojo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchApplyListResponse {
    String userAccount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;
    String userName;
    Integer isAgree;
    Long applyId;
    String problem;
    String answer;
}
