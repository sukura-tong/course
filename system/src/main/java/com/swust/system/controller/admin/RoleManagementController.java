package com.swust.system.controller.admin;

import com.swust.server.dto.*;
import com.swust.server.pojo.RoleResources;
import com.swust.server.service.RoleService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class RoleManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(RoleManagementController.class);
    public static final String BUSINESS_NAME = "角色";

    @Resource
    private RoleService roleService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/queryroleinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        roleService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/saveroleinfo")
    public ResponseDto save(@RequestBody RoleDto roleDto) {
        // 保存校验
        ValidatorUtil.require(roleDto.getName(),"角色");
        ValidatorUtil.length(roleDto.getName(),"角色",1,50);
        ValidatorUtil.require(roleDto.getDesc(),"描述");
        ValidatorUtil.length(roleDto.getDesc(),"描述",1,100);

        ResponseDto responseDto = new ResponseDto();
        roleService.save(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteroleinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        roleService.delete(id);
        return responseDto;
    }

    @RequestMapping(value = "/savecheckresourcesid",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto saveCheckResourcesId(@RequestBody RoleResourcesDto roleResourcesDto){
        ResponseDto responseDto = new ResponseDto();
        roleService.saveInfo(roleResourcesDto);
        return responseDto;
    }

    @GetMapping("/loadroleresources/{id}")
    public ResponseDto loadRoleResources(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        List<String> res = roleService.selectInfoById(id);

        responseDto.setContent(res);
        return responseDto;
    }


    @RequestMapping(value = "/saveroleusers",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto saveRoleUsers(@RequestBody RoleUserDto roleUserDto){
        ResponseDto responseDto = new ResponseDto();
        roleService.saveRoleUsersInfo(roleUserDto);
        return responseDto;
    }

    @GetMapping("/listroleuser/{id}")
    public ResponseDto listRoleUser(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        List<String> res = roleService.selectUserByRoleId(id);

        responseDto.setContent(res);
        return responseDto;
    }
}
