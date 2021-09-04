package com.swust.server.service;

import com.swust.server.pojo.CourseContentFile;
import com.swust.server.pojo.CourseContentFileExample;
import com.swust.server.dto.CourseContentFileDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.CourseContentFileMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseContentFileService {

    @Resource
    private CourseContentFileMapper courseContentFileMapper;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CourseContentFileExample courseContentFileExample = new CourseContentFileExample();
        List<CourseContentFile> courseContentFileList = courseContentFileMapper.selectByExample(courseContentFileExample);

        PageInfo<CourseContentFile> pageInfo = new PageInfo<>(courseContentFileList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(courseContentFileList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(CourseContentFileDto courseContentFileDto) {
        CourseContentFile courseContentFile = CopyUtil.copy(courseContentFileDto, CourseContentFile.class);
        if (StringUtils.isEmpty(courseContentFileDto.getId())) {
            this.insert(courseContentFile);
        } else {
            this.update(courseContentFile);
        }
    }

    /**
     * 新增
     */
    private void insert(CourseContentFile courseContentFile) {

        courseContentFile.setId(UuidUtil.getShortUuid());
        courseContentFileMapper.insert(courseContentFile);
    }

    /**
     * 更新
     */
    private void update(CourseContentFile courseContentFile) {
        courseContentFileMapper.updateByPrimaryKey(courseContentFile);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        courseContentFileMapper.deleteByPrimaryKey(id);
    }

    /***
     * select by id
     * @param courseId
     * @return
     */
    public List<CourseContentFile> getCourseInfoById(String courseId){
        CourseContentFileExample ccfe = new CourseContentFileExample();
        CourseContentFileExample.Criteria criteria = ccfe.createCriteria().andCourseIdEqualTo(courseId);
        List<CourseContentFile> courseContentFiles = courseContentFileMapper.selectByExample(ccfe);

        return courseContentFiles;

    }
}
