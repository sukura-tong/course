package com.swust.server.service;

import com.swust.server.dto.*;
import com.swust.server.enums.CourseStatusEnum;
import com.swust.server.exceptions.CourseOperationException;
import com.swust.server.mapper.CourseContentMapper;
import com.swust.server.mapper.TeacherMapper;
import com.swust.server.mapper.my.MyCourseMapper;
import com.swust.server.pojo.*;
import com.swust.server.mapper.CourseMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private MyCourseMapper myCourseMapper;

    @Resource
    private CourseCategoryService courseCategoryService;

    @Resource
    private CourseContentMapper courseContentMapper;

    @Resource
    private TeacherService teacherService;

    @Resource
    private ChapterService chapterService;

    @Resource
    private SectionService sectionService;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CourseExample courseExample = new CourseExample();
        List<Course> courseList = courseMapper.selectByExample(courseExample);

        PageInfo<Course> pageInfo = new PageInfo<>(courseList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(courseList);
        return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @Transactional
    public void save(CourseDto courseDto) {
        Course course = CopyUtil.copy(courseDto, Course.class);
        if (StringUtils.isEmpty(courseDto.getId())) {
            this.insert(course);
        } else {
            this.update(course);
        }
        // 批量保存课程分类信息
        courseCategoryService.saveBatch(course.getId(),courseDto.getCategorys());
    }

    /**
     * 新增
     */
    private void insert(Course course) {

        // 追加创建时间和修改编辑时间
        Date now = new Date();
        course.setCreateAt(now);
        course.setUpdateAt(now);
        course.setId(UuidUtil.getShortUuid());
        courseMapper.insert(course);
    }

    /**
     * 更新
     */
    private void update(Course course) {
        courseMapper.updateByPrimaryKey(course);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        courseMapper.deleteByPrimaryKey(id);
    }

    public void updateTime(String courseId){
        int effectNum = myCourseMapper.updateTime(courseId);
        if (effectNum <= 0){
         throw new CourseOperationException("err ==> " + "更新课程时长业务操作失败");
        }
    }

    /**
     * 增加课程内容
     * 先更新，更新失败就插入数据
     * @param courseContentDto
     */
    public int insertCourseContentInfo(CourseContentDto courseContentDto){
        CourseContent courseContent = CopyUtil.copy(courseContentDto,CourseContent.class);
        int effectNum = courseContentMapper.updateByPrimaryKeyWithBLOBs(courseContent);
        if (effectNum == 0){
            effectNum = courseContentMapper.insert(courseContent);
        }
        return effectNum;
    }

    /**
     * 查找课程内容
     * @param courseId
     * @return
     */
    public CourseContentDto findCourseContentInfo(String courseId){
        CourseContent courseContent = courseContentMapper.selectByPrimaryKey(courseId);
        if (courseContent == null){
            return null;
        }
        return CopyUtil.copy(courseContent,CourseContentDto.class);
    }

    @Transactional
    public void uodateSortInfo(SortDto sortDto){
        // 修改当前记录
        myCourseMapper.updateSort(sortDto);
        if (sortDto.getNewSort() > sortDto.getOldSort()){
            myCourseMapper.moveSortsForward(sortDto);
        }
        if (sortDto.getOldSort() > sortDto.getNewSort()){
            myCourseMapper.moveSortsBackward(sortDto);
        }
    }

    public List<CourseDto> queryNewCourse(){
        CourseExample cee = new CourseExample();
//        cee.createCriteria().andStatusEqualTo(CourseStatusEnum.PUBLISH.getCode());
//        cee.setOrderByClause("created_at desc");
        List<Course> courses = courseMapper.selectByExample(cee);
        List<CourseDto> courseDtos = CopyUtil.copyList(courses, CourseDto.class);
        return courseDtos;
    }
    public List<CourseDto> queryChooseCourse(CoursePageDto coursePageDto){
        CourseExample cee = new CourseExample();
//        cee.createCriteria().andStatusEqualTo(CourseStatusEnum.PUBLISH.getCode());
//        cee.setOrderByClause("created_at desc");
        List<CourseDto> courses = myCourseMapper.selectCourseByChoose(coursePageDto);
        return courses;
    }

    public CourseDto findCourseById(String courseId){
        Course course = courseMapper.selectByPrimaryKey(courseId);
        if (course == null){
            return null;
        }
        CourseDto courseDto = CopyUtil.copy(course, CourseDto.class);
        // 查询内容
        CourseContent courseContent = courseContentMapper.selectByPrimaryKey(courseId);
        if (courseContent != null){
            courseDto.setContent(courseContent.getContent());
        }
        // 查询讲师信息
        TeacherDto teacherDto = teacherService.findByCourseID(courseDto.getTeacherId());
        courseDto.setTeacher(teacherDto);
        // 查询大章信息
        List<Chapter> chapterList = chapterService.findById(courseId);

        courseDto.setChapters(chapterList);

        // 查询小节信息
        List<Section> sectionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(chapterList)){
            for (Chapter chapter : chapterList){
                String chapterId = chapter.getId();
                List<Section> sections = sectionService.findByCourseIdAndChapterId(courseId, chapterId);
                if (!CollectionUtils.isEmpty(sections)){
                    for (int i = 0; i < sections.size(); i++){
                        sectionList.add(sections.get(i));
                    }
                }
            }
        }
        List<SectionDto> sectionDtos = CopyUtil.copyList(sectionList, SectionDto.class);
        courseDto.setSections(sectionDtos);
        return courseDto;
    }
}
