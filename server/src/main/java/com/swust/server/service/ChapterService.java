package com.swust.server.service;

import com.swust.server.dto.ChapterDto;
import com.swust.server.dto.ChapterPageDto;
import com.swust.server.dto.PageDto;
import com.swust.server.pojo.Chapter;

import java.util.List;

public interface ChapterService {

    ChapterDto getChapterLitst(ChapterPageDto chapterPageDto);

    ChapterDto insertChapterInfo(Chapter chapter);

    ChapterDto modifyChapterInfo(Chapter chapter);

    ChapterDto deleteChapterInfo(Chapter chapter);
    List<Chapter> findById(String id);
}
