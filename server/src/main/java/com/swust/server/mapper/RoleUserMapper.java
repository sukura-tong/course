package com.swust.server.mapper;

import com.swust.server.pojo.RoleUser;
import com.swust.server.pojo.RoleUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleUserMapper {
    long countByExample(RoleUserExample example);

    int deleteByExample(RoleUserExample example);

    int deleteByPrimaryKey(String id);

    int insert(RoleUser record);

    int insertSelective(RoleUser record);

    List<RoleUser> selectByExample(RoleUserExample example);

    RoleUser selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") RoleUser record, @Param("example") RoleUserExample example);

    int updateByExample(@Param("record") RoleUser record, @Param("example") RoleUserExample example);

    int updateByPrimaryKeySelective(RoleUser record);

    int updateByPrimaryKey(RoleUser record);
}