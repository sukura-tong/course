package com.swust.server.dto;

import com.swust.server.enums.ChapterEnum;
import com.swust.server.pojo.Chapter;

import java.util.List;

public class ChapterDto {
    private int state;
    private String stateInfo;
    private Chapter chapter;
    private PageDto<Chapter> chapterPageDto;
    private List<Chapter> chapterList;

    public ChapterDto() {
    }

    public ChapterDto(ChapterEnum chapterEnum) {
        this.state = chapterEnum.getState();
        this.stateInfo = chapterEnum.getStateInfo();
    }

    public ChapterDto(ChapterEnum chapterEnum, Chapter chapter) {
        this.state = chapterEnum.getState();
        this.stateInfo = chapterEnum.getStateInfo();
        this.chapter = chapter;
    }

    public ChapterDto(ChapterEnum chapterEnum, List<Chapter> chapterList, PageDto<Chapter> chapterPageDto) {
        this.state = chapterEnum.getState();
        this.stateInfo = chapterEnum.getStateInfo();
        this.chapterList = chapterList;
        this.chapterPageDto = chapterPageDto;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public Chapter getchapter() {
        return chapter;
    }

    public PageDto<Chapter> getChapterPageDto() {
        return chapterPageDto;
    }

    public List<Chapter> getchapterList() {
        return chapterList;
    }
}
