package com.aimer.controller;

import com.aimer.commen.Result;
import com.aimer.service.TagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/tag")
public class tagController {
    @Resource
    TagService tagService;
    @GetMapping("get")
    public Result<List<Map<String, Object>>> getTags(){
        List<Map<String, Object>> maps = tagService.selectAll();
        return Result.success(maps);
    }
}
