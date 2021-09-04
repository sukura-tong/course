package com.swust.system.controller.admin;

import com.swust.server.dto.RoleUserDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.service.RoleUserService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/roleUser")
public class RoleUserManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(RoleUserManagementController.class);
    public static final String BUSINESS_NAME = "角色用户关联表";

    @Resource
    private RoleUserService roleUserService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/queryroleUserinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        roleUserService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/saveroleUserinfo")
    public ResponseDto save(@RequestBody RoleUserDto roleUserDto) {
        // 保存校验
                    ValidatorUtil.require(roleUserDto.getRoleId(),"角色id");
                    ValidatorUtil.require(roleUserDto.getUserId(),"用户id");

        ResponseDto responseDto = new ResponseDto();
        roleUserService.save(roleUserDto);
        responseDto.setContent(roleUserDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteroleUserinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        roleUserService.delete(id);
        return responseDto;
    }
}
