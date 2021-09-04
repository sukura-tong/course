package com.swust.server.service;

import com.swust.server.dto.RoleResourcesDto;
import com.swust.server.dto.RoleUserDto;
import com.swust.server.mapper.RoleResourcesMapper;
import com.swust.server.mapper.RoleUserMapper;
import com.swust.server.pojo.*;
import com.swust.server.dto.RoleDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.RoleMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;


    @Resource
    private RoleResourcesMapper roleResourcesMapper;

    @Resource
    private RoleUserMapper roleUserMapper;


    private final static Logger LOGGER = LoggerFactory.getLogger(RoleService.class);
    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleExample roleExample = new RoleExample();
        List<Role> roleList = roleMapper.selectByExample(roleExample);

        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(roleList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(RoleDto roleDto) {
        Role role = CopyUtil.copy(roleDto, Role.class);
        if (StringUtils.isEmpty(roleDto.getId())) {
            this.insert(role);
        } else {
            this.update(role);
        }
    }

    /**
     * 新增
     */
    private void insert(Role role) {

        role.setId(UuidUtil.getShortUuid());
        roleMapper.insert(role);
    }

    /**
     * 更新
     */
    private void update(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    public void saveInfo(RoleResourcesDto roleResourcesDto){
        // 每次保存清空库下当前元素的内容
        RoleResourcesExample rree = new RoleResourcesExample();
        rree.createCriteria().andRoleIdEqualTo(roleResourcesDto.getRoleId());
        roleResourcesMapper.deleteByExample(rree);

        String roleId = roleResourcesDto.getRoleId();
        List<String> resourceIds = roleResourcesDto.getResourceIds();

        for (int i = 0; i < resourceIds.size(); i++){
            String resourceId = resourceIds.get(i);
            RoleResources rrs = new RoleResources();
            rrs.setId(UuidUtil.getShortUuid());
            rrs.setRoleId(roleId);
            rrs.setResourcesId(resourceId);
            roleResourcesMapper.insert(rrs);
        }
    }

    public List<String>  selectInfoById(String roleId){
        RoleResourcesExample rree = new RoleResourcesExample();
        rree.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleResources> roleResources = roleResourcesMapper.selectByExample(rree);
        List<String> result = new ArrayList<>();

        System.out.println("角色关联查询记录" + roleResources.size());

        for (RoleResources ele : roleResources){
            result.add(ele.getResourcesId());
        }
        return result;
    }


    public void saveRoleUsersInfo(RoleUserDto roleUserDto){
        String roleId = roleUserDto.getRoleId();
        RoleUserExample rue = new RoleUserExample();
        rue.createCriteria().andRoleIdEqualTo(roleId);
        roleUserMapper.deleteByExample(rue);

        List<String> usersIds = roleUserDto.getUsersIds();
        // 单独插入数据
        for (int i = 0; i < usersIds.size(); i++){
            String userID = usersIds.get(i);
            RoleUser roleUser = new RoleUser();
            roleUser.setId(UuidUtil.getShortUuid());
            roleUser.setRoleId(roleId);
            roleUser.setUserId(userID);

            roleUserMapper.insert(roleUser);
        }
        LOGGER.info("插入数据{}条",usersIds.size());
    }

    public List<String> selectUserByRoleId(String id){
        RoleUserExample rue = new RoleUserExample();
        rue.createCriteria().andRoleIdEqualTo(id);
        List<RoleUser> roleUsers = roleUserMapper.selectByExample(rue);
        List<String> result = new ArrayList<>();

        System.out.println("角色关联查询记录" + roleUsers.size());

        for (RoleUser ele : roleUsers){
            result.add(ele.getUserId());
        }
        return result;
    }
}
