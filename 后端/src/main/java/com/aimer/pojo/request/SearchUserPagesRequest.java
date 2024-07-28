package com.aimer.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SearchUserPagesRequest {
    private List<String> tagNameList;
    private int pageSize;
    private String beginAccount;
    private String sortMethod;
}
