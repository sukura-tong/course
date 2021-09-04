package com.swust.system.controller.admin;

import com.alibaba.fastjson.JSON;
import com.swust.server.constants.LoginUserConstant;
import com.swust.server.dto.LoginUserDto;
import com.swust.server.dto.UserDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.service.UserService;
import com.swust.server.util.UuidUtil;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin/user")
//@CrossOrigin(allowCredentials = "true")
public class UserManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(UserManagementController.class);
    public static final String BUSINESS_NAME = "用户";

    @Resource
    private UserService userService;
//
    @Resource
    public RedisTemplate redisTemplate;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/queryuserinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        userService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/saveuserinfo")
    public ResponseDto save(@RequestBody UserDto userDto) {
        // 保存校验
        ValidatorUtil.require(userDto.getLoginName(),"登录名");
        ValidatorUtil.length(userDto.getLoginName(),"登录名",1,50);
        ValidatorUtil.length(userDto.getName(),"昵称",1,50);
        ValidatorUtil.require(userDto.getPassword(),"密码");

        // 使用springboot提供的md5算法进行密码二次加密
        String md5Password = DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes());
        userDto.setPassword(md5Password);
        ResponseDto responseDto = new ResponseDto();
        userService.save(userDto);
        responseDto.setContent(userDto);
        return responseDto;
    }

    @RequestMapping(value = "/savepassword",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto savePassword(@RequestBody UserDto userDto){
        ResponseDto responseDto = new ResponseDto();
        // 密码二次加密
        String digest = DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes());
        userDto.setPassword(digest);
        userService.editUserPassword(userDto);
        responseDto.setContent(userDto);
        return responseDto;
    }
    /**
     * 删除
     */
    @DeleteMapping("/deleteuserinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        userService.delete(id);
        return responseDto;
    }

    @RequestMapping(value = "/userlogin",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto userLogin(@RequestBody UserDto userDto,
                                 HttpServletRequest request){
        ResponseDto responseDto = new ResponseDto();

        // 登陆校验
        LOG.info("sessionId is " + request.getSession().getId());

//        String sessionImageCode = (String)request.getSession().getAttribute(userDto.getImageCodeToken());
        String sessionImageCode = (String)redisTemplate.opsForValue().get(userDto.getImageCodeToken());
        String userInputImageCode = userDto.getImageCode();

        if (StringUtils.isEmpty(sessionImageCode)){
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码已过期");
            LOG.info("用户登陆失败");
            return responseDto;
        }else if (!sessionImageCode.toLowerCase().equals(userInputImageCode.toLowerCase())){
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码错误");
            LOG.info("用户登陆失败");
            return responseDto;
        }else {
            // 验证通过 移出验证码
//            request.getSession().removeAttribute(userDto.getImageCodeToken());
            redisTemplate.delete(userDto.getImageCodeToken());
        }

        // 密码二次加密
        String digest = DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes());
        userDto.setPassword(digest);

        LoginUserDto login = userService.userLogin(userDto);

        //生成登陆表示
        String token = UuidUtil.getShortUuid();
        login.setToken(token);

        responseDto.setContent(login);

        redisTemplate.opsForValue().set(token, JSON.toJSONString(login),3600, TimeUnit.SECONDS);

//        request.getSession().setAttribute(LoginUserConstant.LOGIN_USER_CONSTANT,login);

        return responseDto;
    }

    @RequestMapping(value = "/userloginout{token}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseDto loginOut(@PathVariable String token, HttpServletRequest request){
        ResponseDto responseDto = new ResponseDto();
//        request.getSession().setAttribute(LoginUserConstant.LOGIN_USER_CONSTANT,null);
        redisTemplate.delete(token);
        LOG.info("退出登陆");
        return responseDto;
    }

}
