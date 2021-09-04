package com.swust.business.controller.web;

import com.swust.server.dto.CourseDto;
import com.swust.server.dto.CoursePageDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//由于admin包下具备相同的方法名，所以需要起别名
@Controller("webCourseManagementController")
@RequestMapping(value = "/web/course")
public class ManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

    public static final String BUSINESS_NAME = "轴承";

    @Resource
    private CourseService courseService;

    @RequestMapping(value = "/listnewcourse",method = RequestMethod.GET)
    @ResponseBody
    public ResponseDto listInfo(){
        PageDto pageDto = new PageDto();
        pageDto.setPage(1);
        pageDto.setSize(3);

        ResponseDto responseDto = new ResponseDto();
        List<CourseDto> courseDtos = courseService.queryNewCourse();
        responseDto.setContent(courseDtos);
        return responseDto;
    }

    @RequestMapping(value = "/listallcourse",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto listAllCourse(@RequestBody CoursePageDto coursePageDto){

        coursePageDto.setPage(1);
        coursePageDto.setSize(3);

        ResponseDto responseDto = new ResponseDto();
        List<CourseDto> courseDtos = courseService.queryChooseCourse(coursePageDto);
        responseDto.setContent(courseDtos);
        return responseDto;
    }

    @GetMapping(value = "/findcoursebyid/{id}")
    @ResponseBody
    public ResponseDto findCourseById(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        CourseDto courseDto = courseService.findCourseById(id);
        responseDto.setContent(courseDto);
        return responseDto;
    }
}
