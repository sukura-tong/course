package com.swust.server.service;

import com.swust.server.pojo.RoleUser;
import com.swust.server.pojo.RoleUserExample;
import com.swust.server.dto.RoleUserDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.RoleUserMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleUserService {

    @Resource
    private RoleUserMapper roleUserMapper;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleUserExample roleUserExample = new RoleUserExample();
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(roleUserExample);

        PageInfo<RoleUser> pageInfo = new PageInfo<>(roleUserList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(roleUserList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(RoleUserDto roleUserDto) {
        RoleUser roleUser = CopyUtil.copy(roleUserDto, RoleUser.class);
        if (StringUtils.isEmpty(roleUserDto.getId())) {
            this.insert(roleUser);
        } else {
            this.update(roleUser);
        }
    }

    /**
     * 新增
     */
    private void insert(RoleUser roleUser) {

        roleUser.setId(UuidUtil.getShortUuid());
        roleUserMapper.insert(roleUser);
    }

    /**
     * 更新
     */
    private void update(RoleUser roleUser) {
        roleUserMapper.updateByPrimaryKey(roleUser);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        roleUserMapper.deleteByPrimaryKey(id);
    }
}
