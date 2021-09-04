package com.swust.server.dto;

import com.swust.server.pojo.Chapter;
import com.swust.server.pojo.Section;

import java.util.List;

public class ChapterSectionDto {
    private Chapter chapter;
    private List<Section> sections;

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
