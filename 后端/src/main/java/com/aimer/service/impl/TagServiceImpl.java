package com.aimer.service.impl;

import com.aimer.mapper.TagMapper;
import com.aimer.pojo.dto.TagDTO;
import com.aimer.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Resource
    TagMapper tagMapper;

    @Override
    public List<Map<String, Object>> selectAll() {
        List<TagDTO> tagList= tagMapper.selectAll();
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
        return resultList;
    }
}
