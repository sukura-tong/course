package com.swust.server.service;

import com.swust.server.dto.CategoryDto;
import com.swust.server.pojo.Category;
import com.swust.server.pojo.CourseCategory;
import com.swust.server.pojo.CourseCategoryExample;
import com.swust.server.dto.CourseCategoryDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.CourseCategoryMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseCategoryService {

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CourseCategoryExample courseCategoryExample = new CourseCategoryExample();
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectByExample(courseCategoryExample);

        PageInfo<CourseCategory> pageInfo = new PageInfo<>(courseCategoryList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(courseCategoryList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(CourseCategoryDto courseCategoryDto) {
        CourseCategory courseCategory = CopyUtil.copy(courseCategoryDto, CourseCategory.class);
        if (StringUtils.isEmpty(courseCategoryDto.getId())) {
            this.insert(courseCategory);
        } else {
            this.update(courseCategory);
        }
    }

    /**
     * 新增
     */
    private void insert(CourseCategory courseCategory) {

        courseCategory.setId(UuidUtil.getShortUuid());
        courseCategoryMapper.insert(courseCategory);
    }

    /**
     * 更新
     */
    private void update(CourseCategory courseCategory) {
        courseCategoryMapper.updateByPrimaryKey(courseCategory);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        courseCategoryMapper.deleteByPrimaryKey(id);
    }

    /***
     * 批量保存
     * @param courseId
     * @param categoryDtos
     */
    @Transactional
    public void saveBatch(String courseId, List<Category> categoryDtos){
        //清除表内数据
        CourseCategoryExample courseCategoryExample = new CourseCategoryExample();
        courseCategoryExample.createCriteria().andCourseIdEqualTo(courseId);
        courseCategoryMapper.deleteByExample(courseCategoryExample);
        //insert
        for (int i = 0; i < categoryDtos.size(); i++){
            Category category = categoryDtos.get(i);
            CourseCategory courseCategory = new CourseCategory();
            courseCategory.setId(UuidUtil.getShortUuid());
            courseCategory.setCourseId(courseId);
            courseCategory.setCategoryId(category.getId());
            insert(courseCategory);
        }
    }

    /**
     * 根据ID查
     * @param courseId
     * @return
     */
    public List<CourseCategoryDto> getInfoByCourseId(String courseId){
        CourseCategoryExample cce = new CourseCategoryExample();
        cce.createCriteria().andCourseIdEqualTo(courseId);
        List<CourseCategory> courseCategories = courseCategoryMapper.selectByExample(cce);
        return CopyUtil.copyList(courseCategories,CourseCategoryDto.class);
    }
}
