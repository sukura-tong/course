package com.swust.server.service;

import com.alibaba.fastjson.JSON;
import com.swust.server.dto.LoginUserDto;
import com.swust.server.dto.ResourcesDto;
import com.swust.server.exceptions.BusinessException;
import com.swust.server.exceptions.BusinessExceptionCode;
import com.swust.server.mapper.my.MyUserMapper;
import com.swust.server.pojo.User;
import com.swust.server.pojo.UserExample;
import com.swust.server.dto.UserDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.UserMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyUserMapper myUserMapper;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        UserExample userExample = new UserExample();
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(userList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(UserDto userDto) {
        User user = CopyUtil.copy(userDto, User.class);
        if (StringUtils.isEmpty(userDto.getId())) {
            this.insert(user);
        } else {
            this.update(user);
        }
    }

    /**
     * 新增
     */
    private void insert(User user) {

        user.setId(UuidUtil.getShortUuid());
        User userDb = this.seleteByLoginName(user.getLoginName());
        if (userDb != null){
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
        userMapper.insert(user);
    }

    /**
     * 更新
     */
    private void update(User user) {
        // selective会对字段进行非空验证，如果字段为空就不更新
        user.setPassword(null);
//        userMapper.updateByPrimaryKey(user);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据登录名查询用户信息
     * @param loginName
     * @return
     */
    public User seleteByLoginName(String loginName){
        UserExample ue = new UserExample();
        ue.createCriteria().andLoginNameEqualTo(loginName);
        List<User> users = userMapper.selectByExample(ue);

        if (CollectionUtils.isEmpty(users)){
            return null;
        }else {
            return users.get(0);
        }
    }

    public void editUserPassword(UserDto userDto){
        String id = userDto.getId();
        UserExample ue = new UserExample();
        ue.createCriteria().andIdEqualTo(id);

        User user = new User();
        user.setPassword(userDto.getPassword());

        int effect = userMapper.updateByExampleSelective(user, ue);

        if (effect < 1){
            throw new RuntimeException("密码重置错误");
        }
    }

    /***
     * 查找用户名后比对密码
     * @param userDto
     * @return
     */
    public LoginUserDto userLogin(UserDto userDto){
        User user = seleteByLoginName(userDto.getLoginName());
        if (user == null){
            // 用户名不存在
            logger.info("用户名不存在");
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else {
            if (!user.getPassword().equals(userDto.getPassword())){
                // 登录succeess
                // 绑定用户登陆权限
                LoginUserDto loginUserDto = CopyUtil.copy(user, LoginUserDto.class);
                setAuth(loginUserDto);
                return loginUserDto;
            }else {
                logger.info("用户名密码错误");
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    /**
     * 为登陆用户读取权限
     * @param loginUserDto
     */
    private void setAuth(LoginUserDto loginUserDto){
        List<ResourcesDto> resourcesDtoList = myUserMapper.findResources(loginUserDto.getId());
        loginUserDto.setResources(resourcesDtoList);

        // 整理所有权限 用于接口拦截
        HashSet<String> requestSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(resourcesDtoList)){
            for (int i = 0; i < resourcesDtoList.size(); i++){
                ResourcesDto resourcesDto = resourcesDtoList.get(i);
                String request = resourcesDto.getRequest();
                List<String> requestList = JSON.parseArray(request, String.class);

                if (!CollectionUtils.isEmpty(requestList)){
                    requestSet.addAll(requestList);
                }
            }
        }
        logger.info("所有权限添加完毕");
        loginUserDto.setRequests(requestSet);
    }
}
