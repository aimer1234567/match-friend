package com.aimer.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apply {
    Long id;
    Integer isAgree;
    String acceptUserAccount;
    String applyUserAccount;
    String answer;
    String introduction;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    String problem;
}
