package com.swust.business.controller.admin;

import com.swust.server.dto.CategoryDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.service.CategoryService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/category")
public class CategoryManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryManagementController.class);
    public static final String BUSINESS_NAME = "分类表";

    @Resource
    private CategoryService categoryService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/querycategoryinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list() {
        PageDto pageDto = new PageDto();
        pageDto.setSize(1);
        pageDto.setPage(1);

        ResponseDto responseDto = new ResponseDto();
        categoryService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/savecategoryinfo")
    public ResponseDto save(@RequestBody CategoryDto categoryDto) {
        // 保存校验
                    ValidatorUtil.require(categoryDto.getParent(),"父id");
                    ValidatorUtil.require(categoryDto.getName(),"名称");
                    ValidatorUtil.length(categoryDto.getName(),"名称",1,50);

        ResponseDto responseDto = new ResponseDto();
        categoryService.save(categoryDto);
        responseDto.setContent(categoryDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deletecategoryinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        categoryService.delete(id);
        return responseDto;
    }
}
