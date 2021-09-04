package com.swust.business.controller.admin;

import com.swust.server.dto.CourseContentFileDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.pojo.CourseContentFile;
import com.swust.server.service.CourseContentFileService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/course-content-file")
public class CourseContentFileManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseContentFileManagementController.class);
    public static final String BUSINESS_NAME = "课程内容文件";

    @Resource
    private CourseContentFileService courseContentFileService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/querycoursecontentfileinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        courseContentFileService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/savecoursecontentfileinfo")
    public ResponseDto save(@RequestBody CourseContentFileDto courseContentFileDto) {
        // 保存校验
        ValidatorUtil.require(courseContentFileDto.getCourseId(),"课程id");
        ValidatorUtil.length(courseContentFileDto.getUrl(),"地址",1,100);
        ValidatorUtil.length(courseContentFileDto.getName(),"文件名",1,100);

        ResponseDto responseDto = new ResponseDto();
        courseContentFileService.save(courseContentFileDto);
        responseDto.setContent(courseContentFileDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deletecoursecontentfileinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        courseContentFileService.delete(id);
        return responseDto;
    }

    @GetMapping(value = "/querycourseinfobyid/{courseId}")
    public ResponseDto queryCourseInfoById(@PathVariable String courseId){
        ResponseDto responseDto = new ResponseDto();
        List<CourseContentFile> info = courseContentFileService.getCourseInfoById(courseId);
        responseDto.setContent(info);
        responseDto.setSuccess(true);
        return responseDto;
    }
}
