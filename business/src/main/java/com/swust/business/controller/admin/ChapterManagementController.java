package com.swust.business.controller.admin;

import com.swust.server.dto.ChapterDto;
import com.swust.server.dto.ChapterPageDto;
import com.swust.server.dto.PageDto;
import com.swust.server.exceptions.ChapterOperationException;
import com.swust.server.exceptions.ValidatorException;
import com.swust.server.pojo.Chapter;
import com.swust.server.service.ChapterService;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/chapter")
public class ChapterManagementController {

    @Autowired
    private ChapterService chapterService;

    // 业务名称 供AOP反射使用
    public static final String BUSSINESS_NAME = "大章";

    private static Logger logger = LoggerFactory.getLogger(ChapterManagementController.class);

    @RequestMapping(value = "/querychapterinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryChapterInfo(@RequestBody ChapterPageDto chapterPageDto) {
        Map<String, Object> modelMap = new HashMap<>();
        ChapterDto ce = null;
        // 前端校验
        ValidatorUtil.require(chapterPageDto.getCourseId(),"课程ID");
        // 业务主体
        try {
            ce = chapterService.getChapterLitst(chapterPageDto);
        } catch (ChapterOperationException exception) {
            logger.error("err ==> " + exception.getMessage());
            modelMap.put("success", false);
            modelMap.put("errMsg", exception.getMessage());
            return modelMap;
        }

        if (ce != null) {
            if (ce.getState() != 1) {
                logger.error("err ==> " + ce.getStateInfo());
                modelMap.put("success", false);
                modelMap.put("errMsg", ce.getStateInfo());
                return modelMap;
            } else {
                logger.info("success ==> " + ce.getStateInfo());
                modelMap.put("success", true);
                System.out.println(ce.getchapterList());
                modelMap.put("chapterInfo", ce.getchapterList());
                if (ce.getChapterPageDto() == null){
                    logger.info("分页代码失效");
                    PageDto dto = new PageDto();
                    dto.setTotal(2);
                    dto.setPage(1);
                    dto.setSize(2);
                    modelMap.put("pageDtoInfos",dto);
                }else {
                    PageDto dto = new PageDto();
                    dto.setTotal(2);
                    dto.setPage(1);
                    dto.setSize(2);
                    modelMap.put("pageDtoInfos",dto);
//                modelMap.put("pageDtoInfos",ce.getChapterPageDto());
                }
                return modelMap;
            }
        }else {
            logger.info("success ==> " + ce.getStateInfo());
            modelMap.put("success", true);
            modelMap.put("chaapterInfos", ce.getchapterList());
            return modelMap;
        }
    }

    @RequestMapping(value = "/savechapterinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveChapterInfo(@RequestBody Chapter chapter){
        Map<String, Object> modelMap = new HashMap<>();

        // 前端空值安全校验
        if (chapter == null){
            modelMap.put("success",false);
            modelMap.put("errMsg","chapter 信息为空");
            return modelMap;
        }
        try {
            //安全校验
            ValidatorUtil.require(chapter.getName(),"名称");
            ValidatorUtil.require(chapter.getCourseId(),"课程ID");
            ValidatorUtil.length(chapter.getCourseId(),"课程ID",1,8);
        }catch (ValidatorException exception){
            logger.error("err ==> " + exception.getMessage());
            modelMap.put("success", false);
            modelMap.put("errMsg", exception.getMessage());
            return modelMap;
        }

        ChapterDto cd = null;
        try {
            if (chapter.getId() != null){
                cd = chapterService.modifyChapterInfo(chapter);
            }else {
                cd = chapterService.insertChapterInfo(chapter);
            }
        }catch (ChapterOperationException exception){
            logger.error("err ==> " + exception.getMessage());
            modelMap.put("success", false);
            modelMap.put("errMsg", exception.getMessage());
            return modelMap;
        }

        if (cd == null){
            logger.error("err ==> " + "业务逻辑返回信息为空，业务操作失败");
            modelMap.put("success", false);
            modelMap.put("errMsg",  "业务逻辑返回信息为空，业务操作失败");
            return modelMap;
        }
        if (cd.getState() != 1){
            logger.error("err ==> " + cd.getStateInfo());
            modelMap.put("success", false);
            modelMap.put("errMsg", cd.getStateInfo());
            return modelMap;
        }
        //插入成功返回正确结果
        logger.info("chapter数据操作成功！！！");
        modelMap.put("success",true);
        modelMap.put("chapterInfo",cd.getchapter());
        return modelMap;
    }

    @RequestMapping(value = "/deletechapterinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteChapterInfo(@RequestBody Chapter chapter){
        Map<String, Object> modelMap = new HashMap<>();

        // 前端空值安全校验
        if (chapter == null || chapter.getId() == null){
            modelMap.put("success",false);
            modelMap.put("errMsg","chapter 信息为空");
            return modelMap;
        }
        ChapterDto cd = null;
        try {
            cd = chapterService.deleteChapterInfo(chapter);
        }catch (ChapterOperationException exception){
            logger.error("err ==> " + exception.getMessage());
            modelMap.put("success", false);
            modelMap.put("errMsg", exception.getMessage());
            return modelMap;
        }
        if (cd == null){
            logger.error("err ==> " + "业务逻辑返回信息为空，业务操作失败");
            modelMap.put("success", false);
            modelMap.put("errMsg",  "业务逻辑返回信息为空，业务操作失败");
            return modelMap;
        }
        if (cd.getState() != 1){
            logger.error("err ==> " + cd.getStateInfo());
            modelMap.put("success", false);
            modelMap.put("errMsg", cd.getStateInfo());
            return modelMap;
        }
        //插入成功返回正确结果
        logger.info("chapter数据操作成功！！！");
        modelMap.put("success",true);
        return modelMap;
    }
}

