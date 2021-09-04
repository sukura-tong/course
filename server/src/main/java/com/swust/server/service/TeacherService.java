package com.swust.server.service;

import com.swust.server.pojo.Teacher;
import com.swust.server.pojo.TeacherExample;
import com.swust.server.dto.TeacherDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.TeacherMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        TeacherExample teacherExample = new TeacherExample();
        List<Teacher> teacherList = teacherMapper.selectByExample(teacherExample);

        PageInfo<Teacher> pageInfo = new PageInfo<>(teacherList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(teacherList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(TeacherDto teacherDto) {
        Teacher teacher = CopyUtil.copy(teacherDto, Teacher.class);
        if (StringUtils.isEmpty(teacherDto.getId())) {
            this.insert(teacher);
        } else {
            this.update(teacher);
        }
    }

    /**
     * 新增
     */
    private void insert(Teacher teacher) {
        teacher.setId(UuidUtil.getShortUuid());
        teacherMapper.insert(teacher);
    }

    /**
     * 更新
     */
    private void update(Teacher teacher) {
        teacherMapper.updateByPrimaryKey(teacher);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        teacherMapper.deleteByPrimaryKey(id);
    }


    public List<TeacherDto> searchAll(){
        TeacherExample example = new TeacherExample();
        List<Teacher> teachers = teacherMapper.selectByExample(example);
        List<TeacherDto> teacherDtos = CopyUtil.copyList(teachers, TeacherDto.class);
        return teacherDtos;
    }

    public TeacherDto findByCourseID(String id){
        Teacher teacher = teacherMapper.selectByPrimaryKey(id);
        TeacherDto teacherDto = CopyUtil.copy(teacher, TeacherDto.class);
        return teacherDto;
    }
}
