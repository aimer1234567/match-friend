package com.aimer;

import com.aimer.mapper.ApplyMapper;
import com.aimer.mapper.TagMapper;
import com.aimer.pojo.VO.UserVO;
import com.aimer.pojo.dto.TagDTO;
import com.aimer.pojo.response.SearchApplyListResponse;
import com.aimer.service.UserService;
import com.aimer.utils.AddressCoding;
import com.aimer.utils.OSS;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class MatchApplicationTests {
    @Resource
    TagMapper tagMapper;
    @Resource
    UserService userService;
    @Resource
    ApplyMapper applyMapper;
    @Test
    public void test(){  //TODO 学习stream流
        List<TagDTO> tagList = tagMapper.selectAll();
        // 使用 Stream API 进行处理
        List<Map<String, Object>> resultList = tagList.stream()
                // 使用 Collectors.groupingBy 将标签按照父标签进行分组
                .collect(Collectors.groupingBy(TagDTO::getParentName))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("text", entry.getKey());
                    map.put("children", entry.getValue().stream()
                            .map(tag -> {
                                Map<String, String> childMap = new HashMap<>();
                                childMap.put("text", tag.getTagName());
                                childMap.put("id", tag.getTagName()); // 用标签名作为 id
                                return childMap;
                            })
                            .collect(Collectors.toList()));
                    return map;
                })
                .collect(Collectors.toList());

        // 输出结果
        System.out.println(resultList);
    }
    @Test
    public void test2(){
        List<BigDecimal> list = AddressCoding.parse("福州市鼓楼区");
        log.info(list.toString());
    }
    @Test
    public void sortBySimilar(){
        List<UserVO> userVOS = userService.searchUsersByTagsInitial(Arrays.asList("羽毛球"));
        for (UserVO userVO : userService.sortBySimilar(userVOS)) {
            log.info(userVO.toString());
        }
    }
    @Test
    public void sortByAge(){
        List<UserVO> userVOS = userService.searchUsersByTagsInitial(Arrays.asList("羽毛球"));
        for(UserVO userVO:userService.sortByAge(userVOS)){
            log.info(userVO.toString());
        }
    }

    @Test
    public void ossGetFile() throws UnsupportedEncodingException {
        OSS oss = new OSS();
        oss.deleteFile("defe35ddeac78f04a371d5a308441522.jpg");
    }
    @Test
    public void mapper(){
        List<SearchApplyListResponse> searchApplyListResponses = applyMapper.queryApplyTo("123", new Date(), 0, 10);

    }

}
