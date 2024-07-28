package com.aimer.service;

import com.aimer.funation.UserSort;
import com.aimer.pojo.VO.UserDetailVO;
import com.aimer.pojo.VO.UserVO;
import com.aimer.pojo.dto.RegisterDTO;
import com.aimer.pojo.entity.User;
import com.aimer.pojo.request.LoginRequest;
import com.aimer.pojo.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    public void register(RegisterDTO registerDTO);

    List<UserVO> searchUsersByTagsInitial(List<String> tagNameList);

    List<UserVO> sortBySimilar(List<UserVO> userList);

    List<UserVO> sortByAge(List<UserVO> userList);

    String login(LoginRequest loginRequest, HttpServletResponse response);

    User getUserInformation() throws UnsupportedEncodingException;

    void updateUserInformation(UpdateUserRequest updateUserRequest);

    List<UserVO> sortByAddress(List<UserVO> userList);

    List<UserVO> searchUsersByTags(List<String> tagNameList, Integer pageSize, UserSort userSort);

    List<UserVO> searchUsersByTagsSort(List<String> tagNameList, Integer pageSize, String sortMethod);
    void updateAvatarUrl(MultipartFile file) throws IOException;

    UserDetailVO getUserDetailVOByUserAccount(String userAccount);
}
