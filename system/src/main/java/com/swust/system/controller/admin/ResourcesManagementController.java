package com.swust.system.controller.admin;

import com.swust.server.dto.ResourcesDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.service.ResourcesService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/resources")
public class ResourcesManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourcesManagementController.class);
    public static final String BUSINESS_NAME = "资源";

    @Resource
    private ResourcesService resourcesService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/queryresourcesinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        resourcesService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/saveresourcesinfo")
    public ResponseDto save(@RequestBody ResourcesDto resourcesDto) {
        // 保存校验
                    ValidatorUtil.require(resourcesDto.getName(),"名称");
                    ValidatorUtil.length(resourcesDto.getName(),"名称",1,100);
                    ValidatorUtil.length(resourcesDto.getPage(),"页面",1,50);
                    ValidatorUtil.length(resourcesDto.getRequest(),"请求",1,400);

        ResponseDto responseDto = new ResponseDto();
        resourcesService.save(resourcesDto);
        responseDto.setContent(resourcesDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteresourcesinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        resourcesService.delete(id);
        return responseDto;
    }

    @GetMapping(value = "/loadtree")
    @ResponseBody
    public ResponseDto loadTree(){
        ResponseDto responseDto = new ResponseDto();
        List<ResourcesDto> resourcesDtos = resourcesService.loadTree();
        responseDto.setContent(resourcesDtos);
        return responseDto;
    }
}
