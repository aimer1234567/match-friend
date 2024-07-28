package com.aimer.controller;

import com.aimer.commen.Result;
import com.aimer.pojo.entity.User;
import com.aimer.utils.OSS;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@Tag(name="测试")
@RequestMapping("/test")
public class testController {
    @PostMapping("/1")
    public Result<User> test(@RequestParam String userPassword, HttpServletRequest httpServletRequest){
        log.info(userPassword);
        log.info(httpServletRequest.toString());
        User build = User.builder().id(1L).gender(1).userPassword("1221").build();
        return Result.success(build);
    }

}
