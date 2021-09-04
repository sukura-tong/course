package com.swust.business.controller.web;

import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

//由于admin包下具备相同的方法名，所以需要起别名
@Controller("webCategoryManagementController")
@RequestMapping(value = "/web/course")
public class CategoryManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryManagementController.class);
    public static final String BUSINESS_NAME = "分类";

    @Resource
    private CategoryService categoryService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/listallcategory",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto lislistAllCategoryt() {
        PageDto pageDto = new PageDto();
        pageDto.setSize(1);
        pageDto.setPage(1);

        ResponseDto responseDto = new ResponseDto();
        categoryService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }
}
