package com.swust.business.controller.admin;

import com.swust.server.dto.*;
import com.swust.server.service.CourseCategoryService;
import com.swust.server.service.CourseService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/course")
public class CourseManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseManagementController.class);
    public static final String BUSINESS_NAME = "课程表";

    @Resource
    private CourseService courseService;

    @Resource
    private CourseCategoryService courseCategoryService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/querycourseinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        courseService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/savecourseinfo")
    public ResponseDto save(@RequestBody CourseDto courseDto) {
        // 保存校验
        ValidatorUtil.require(courseDto.getName(),"名称");
        ValidatorUtil.length(courseDto.getName(),"名称",1,50);
        ValidatorUtil.length(courseDto.getSummary(),"概述",1,2000);
        ValidatorUtil.require(courseDto.getPrice(),"价格(元)");
        ValidatorUtil.length(courseDto.getImage(),"封面",1,100);
        ValidatorUtil.require(courseDto.getLevel(),"级别");

        ResponseDto responseDto = new ResponseDto();
        courseService.save(courseDto);
        responseDto.setContent(courseDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deletecourseinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        courseService.delete(id);
        return responseDto;
    }

    /**
     * 根据 ID查询
     * @param courseId
     * @return
     */
    @PostMapping(value = "/listcategoryInfo/{courseId}")
    public ResponseDto listCategory(@PathVariable String courseId){
        ResponseDto responseDto = new ResponseDto();
        List<CourseCategoryDto> categoryDtos = courseCategoryService.getInfoByCourseId(courseId);
        responseDto.setContent(categoryDtos);
        return responseDto;
    }

    @PostMapping(value = "/findcontentinfo/{courseId}")
    public ResponseDto findContentInfo(@PathVariable String courseId){
        ResponseDto responseDto = new ResponseDto();
        CourseContentDto courseContentInfo = courseService.findCourseContentInfo(courseId);
        responseDto.setContent(courseContentInfo);
        return responseDto;
    }

    @PostMapping(value = "/savecontentinfo")
    public ResponseDto saveContentInfo(@RequestBody CourseContentDto courseContentDto){
        ResponseDto responseDto = new ResponseDto();
        int effectNum = courseService.insertCourseContentInfo(courseContentDto);
        responseDto.setSuccess(true);
        return responseDto;
    }

    @PostMapping(value = "/savesortinfo")
    public ResponseDto saveSortInfo(@RequestBody SortDto sortDto){
        ResponseDto responseDto = new ResponseDto();
        courseService.uodateSortInfo(sortDto);
        responseDto.setSuccess(true);
        return responseDto;
    }
}
