package com.aimer.controller;

import com.aimer.commen.BaseContext;
import com.aimer.commen.Result;
import com.aimer.commen.validator.NullValidator;
import com.aimer.mapper.UserMapper;
import com.aimer.pojo.VO.UserDetailVO;
import com.aimer.pojo.VO.UserVO;
import com.aimer.pojo.dto.RegisterDTO;
import com.aimer.pojo.entity.User;
import com.aimer.pojo.request.LoginRequest;
import com.aimer.pojo.request.UpdateUserRequest;
import com.aimer.service.UserService;
import com.aimer.utils.SseClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/user")
@Tag(name = "用户操作")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private SseClient sseClient;
    @Resource
    private UserMapper userMapper;
    /**
     * 注册
     */
    @PostMapping("/register")
    @Operation(summary = "注册")
    public Result<Object> register(@RequestBody RegisterDTO registerDTO){
        NullValidator.validate(registerDTO);
        userService.register(registerDTO);
        return Result.success();
    }
    /**
     * 登录
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        NullValidator.validate(loginRequest);
        String token = userService.login(loginRequest, response);
        return Result.success(token);
    }

    /**
     * 根据标签搜索用户
     * @param
     * @return
     */

    @GetMapping("/search/tags")
    @Operation(summary = "通过标签搜索")
    public  Result<List<UserVO>> searchUsersByTags(@RequestParam(value = "tagNameArray",defaultValue = "isEmpty") String[] tagNameArray,@RequestParam("pageSize")Integer pageSize,@RequestParam String sortMethod) {
        List<String> tagNameList=null;
        if(!Objects.equals(tagNameArray[0], "isEmpty")){
            tagNameList = Arrays.asList(tagNameArray);
        }
        List<UserVO> userList = userService.searchUsersByTagsSort(tagNameList,pageSize,sortMethod);
        return Result.success(userList);
    }
    /**
     * 获取用户基本信息
     */
    @GetMapping("/information")
    @Operation(summary = "查询个人基本信息")
    public Result<User> getUserInformation() throws UnsupportedEncodingException {
        User userInformation = userService.getUserInformation();
        return Result.success(userInformation);
    }

    /**
     * 修改用户基本信息
     */
//TODO 传递参数存在空值，且不能进行检验不安全，后期进行修改
    @PostMapping("/information/update")
    @Operation(summary = "修改个人基本信息")
    public Result<Object> updateUserInformation(@RequestBody UpdateUserRequest updateUserRequest){
        NullValidator.validate(updateUserRequest);
        userService.updateUserInformation(updateUserRequest);
        return Result.success();
    }

    /**
     * 更新头像
     * @param file
     * @return
     * @throws IOException
     */

    @PostMapping("/avatarUrl")
    @Operation(summary = "更新头像")
    public Result<Object> updateAvatarUrl(@RequestParam("file") MultipartFile file) throws IOException {
        userService.updateAvatarUrl(file);
        return Result.success();
    }

    /**
     * 获取用户主页
     * @param userAccount
     * @return
     */
    @GetMapping("/space/{userAccount}")
    @Operation(summary = "获取用户主页信息")
    public Result<UserDetailVO> getUserDetail(@PathVariable String userAccount){
        return Result.success(userService.getUserDetailVOByUserAccount(userAccount));
    }
    /**
     * 建立SSE连接
     */
    @GetMapping("/sse")
    public SseEmitter createConnect(){
        Long id = BaseContext.getId();
        String userAccount = userMapper.getUserInformation(id).getUserAccount();
        return sseClient.createSse(userAccount);
    }
}
