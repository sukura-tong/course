package com.swust.business.controller.admin;

import com.swust.server.dto.SectionDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.dto.SectionPageDto;
import com.swust.server.service.SectionService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/section")
public class SectionManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(SectionManagementController.class);
    public static final String BUSINESS_NAME = "小节";

    @Resource
    private SectionService sectionService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/querysectioninfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto querySectionInfo(@RequestBody SectionPageDto sectionPageDto) {
        // 前端数据校验
        ValidatorUtil.require(sectionPageDto.getChapterId(),"大章ID");
        ValidatorUtil.require(sectionPageDto.getCourseId(),"课程ID");

        ResponseDto responseDto = new ResponseDto();
        SectionPageDto dto = sectionService.list(sectionPageDto);
        responseDto.setContent(dto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/savesectioninfo")
    public ResponseDto save(@RequestBody SectionDto sectionDto) {
        // 保存校验
//        ValidatorUtil.require(sectionDto.getName(), "名称");
        ValidatorUtil.require(sectionDto.getCourseId(), "课程ID");
        ValidatorUtil.length(sectionDto.getCourseId(), "课程ID", 1, 8);

        ResponseDto responseDto = new ResponseDto();
        sectionService.save(sectionDto);
        responseDto.setContent(sectionDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        sectionService.delete(id);
        return responseDto;
    }
}
