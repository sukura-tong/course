package com.swust.server.service;

import com.swust.server.dto.SectionPageDto;
import com.swust.server.enums.SectionChargeEnum;
import com.swust.server.pojo.Section;
import com.swust.server.pojo.SectionExample;
import com.swust.server.dto.SectionDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.SectionMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SectionService {

    @Resource
    private SectionMapper sectionMapper;

    @Resource
    private CourseService courseService;

    /**
     * 列表查询
     */
    public SectionPageDto list(SectionPageDto sectionPageDto) {
        PageHelper.startPage(sectionPageDto.getPage(), sectionPageDto.getSize());
        SectionExample sectionExample = new SectionExample();
        SectionExample.Criteria criteria = sectionExample.createCriteria();

        if (!StringUtils.isEmpty(sectionPageDto.getCourseId())){
            criteria.andCourseIdEqualTo(sectionPageDto.getCourseId());
        }

        if (!StringUtils.isEmpty(sectionPageDto.getChapterId())){
            criteria.andChapterIdEqualTo(sectionPageDto.getChapterId());
        }
        List<Section> sectionList = sectionMapper.selectByExample(sectionExample);
        PageInfo<Section> pageInfo = new PageInfo<>(sectionList);
        sectionPageDto.setTotal(pageInfo.getTotal());

        sectionPageDto.setList(sectionList);
        return sectionPageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(SectionDto sectionDto) {
        Section section = CopyUtil.copy(sectionDto, Section.class);
        if (StringUtils.isEmpty(sectionDto.getId())) {
            this.insert(section);
        } else {
            this.update(section);
        }
        courseService.updateTime(sectionDto.getCourseId());
    }

    /**
     * 新增
     */
    private void insert(Section section) {
        section.setId(UuidUtil.getShortUuid());
        Date date = new Date();
        section.setCreatedAt(date);
        section.setUpdatedAt(date);
        section.setCharge(SectionChargeEnum.CHARGE.getCode());
        sectionMapper.insert(section);
    }

    /**
     * 更新
     */
    private void update(Section section) {
        sectionMapper.updateByPrimaryKey(section);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        sectionMapper.deleteByPrimaryKey(id);
    }

    public List<Section>  findByCourseIdAndChapterId(String courseId, String chapterId){
        SectionExample see = new SectionExample();
        see.createCriteria().andChapterIdEqualTo(chapterId).andCourseIdEqualTo(courseId);

        List<Section> sections = sectionMapper.selectByExample(see);
        return sections;
    }
}
