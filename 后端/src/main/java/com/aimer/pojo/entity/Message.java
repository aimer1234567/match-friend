package com.aimer.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Context;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String SendUserAccount;
    private String ReceiveUserAccount;
    private String id;
    private Apply context;
    private Integer isRead;
    private Long contextId;
}
