package com.aimer.mapper;

import com.aimer.pojo.dto.TagDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    List<TagDTO> selectAll();
}
