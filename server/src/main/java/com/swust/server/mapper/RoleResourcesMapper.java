package com.swust.server.mapper;

import com.swust.server.pojo.RoleResources;
import com.swust.server.pojo.RoleResourcesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleResourcesMapper {
    long countByExample(RoleResourcesExample example);

    int deleteByExample(RoleResourcesExample example);

    int deleteByPrimaryKey(String id);

    int insert(RoleResources record);

    int insertSelective(RoleResources record);

    List<RoleResources> selectByExample(RoleResourcesExample example);

    RoleResources selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") RoleResources record, @Param("example") RoleResourcesExample example);

    int updateByExample(@Param("record") RoleResources record, @Param("example") RoleResourcesExample example);

    int updateByPrimaryKeySelective(RoleResources record);

    int updateByPrimaryKey(RoleResources record);
}