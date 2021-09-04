package com.swust.server.mapper.my;

import com.swust.server.dto.ResourcesDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyUserMapper {
    List<ResourcesDto> findResources(@Param("userId") String userId);
}
