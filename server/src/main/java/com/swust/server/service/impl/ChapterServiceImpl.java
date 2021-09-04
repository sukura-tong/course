package com.swust.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swust.server.dto.ChapterPageDto;
import com.swust.server.dto.PageDto;
import com.swust.server.enums.ChapterEnum;
import com.swust.server.dto.ChapterDto;
import com.swust.server.exceptions.ChapterOperationException;
import com.swust.server.mapper.ChapterMapper;
import com.swust.server.pojo.Chapter;
import com.swust.server.pojo.ChapterExample;
import com.swust.server.service.ChapterService;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.swing.event.ChangeListener;
import java.nio.file.Path;
import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    private static Logger logger = LoggerFactory.getLogger(ChapterServiceImpl.class);

    @Override
    public ChapterDto getChapterLitst(ChapterPageDto chapterPageDto) {

        ChapterDto te = null;

        List<Chapter> chapterList = null;
        try {
            PageHelper.startPage(chapterPageDto.getPage(),chapterPageDto.getSize());
            ChapterExample ce = new ChapterExample();
            // criteria只允许一次
            ChapterExample.Criteria criteria = ce.createCriteria();
            if (!StringUtils.isEmpty(chapterPageDto.getCourseId())){
                criteria.andCourseIdEqualTo(chapterPageDto.getCourseId());
            }
            chapterList = chapterMapper.selectByExample(ce);

            PageInfo<Chapter> pageInfo = new PageInfo<>(chapterList);
            chapterPageDto.setTotal(pageInfo.getTotal());
            chapterPageDto.setList(chapterList);
        }catch (ChapterOperationException execution){
            execution.printStackTrace();
            logger.error("err ==> " + execution.getMessage());
        }
        te = new ChapterDto(ChapterEnum.SUCCESS,chapterList,chapterPageDto);
        return te;
    }

    @Override
    @Transactional
    public ChapterDto insertChapterInfo(Chapter chapter) {
        ChapterDto cd = null;
        // 设置ID
        chapter.setId(UuidUtil.getShortUuid());
        int effectNum = 0;
        try {
             effectNum = chapterMapper.insert(chapter);
        }catch (ChapterOperationException exception){
            exception.printStackTrace();
            logger.error("err ==> " + exception.getMessage());
        }

        if (effectNum != 1){
            cd = new ChapterDto(ChapterEnum.ERROR,chapter);
            return cd;
        }
        cd = new ChapterDto(ChapterEnum.SUCCESS,chapter);
        return cd;
    }

    /**
     * 更新方法
     * @param chapter
     * @return
     */
    @Override
    public ChapterDto modifyChapterInfo(Chapter chapter) {
        ChapterDto cd = null;
        // 查看ID字段是否为空
        if (StringUtils.isEmpty(chapter.getId())){
            cd = new ChapterDto(ChapterEnum.CHECK,chapter);
            return cd;
        }
        int effectNum = 0;
        try {
            effectNum = chapterMapper.updateByPrimaryKey(chapter);
        }catch (ChapterOperationException exception){
            exception.printStackTrace();
            logger.error("err ==> " + exception.getMessage());
        }

        if (effectNum != 1){
            cd = new ChapterDto(ChapterEnum.ERROR,chapter);
            return cd;
        }
        cd = new ChapterDto(ChapterEnum.SUCCESS,chapter);
        return cd;
    }

    @Override
    public ChapterDto deleteChapterInfo(Chapter chapter) {
        String chapterId = chapter.getId();
        ChapterDto cd = null;
        // 查看ID字段是否为空
        if (StringUtils.isEmpty(chapter.getId())){
            cd = new ChapterDto(ChapterEnum.CHECK,chapter);
            return cd;
        }

        int effectNum = 0;
        try {
            effectNum = chapterMapper.deleteByPrimaryKey(chapterId);
        }catch (ChapterOperationException exception){
            exception.printStackTrace();
            logger.error("err ==> " + exception.getMessage());
        }

        if (effectNum != 1){
            cd = new ChapterDto(ChapterEnum.ERROR,chapter);
            return cd;
        }
        cd = new ChapterDto(ChapterEnum.SUCCESS,chapter);
        return cd;
    }

    public List<Chapter> findById(String courseId){
       ChapterExample cee = new ChapterExample();
       cee.createCriteria().andCourseIdEqualTo(courseId);
        List<Chapter> chapterList = chapterMapper.selectByExample(cee);
        return chapterList;
    }

}
