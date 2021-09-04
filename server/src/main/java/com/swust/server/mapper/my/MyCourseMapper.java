package com.swust.server.mapper.my;

import com.swust.server.dto.CourseDto;
import com.swust.server.dto.CoursePageDto;
import com.swust.server.dto.SortDto;
import com.swust.server.pojo.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyCourseMapper {
    int updateTime(@Param("courseId") String courseId);

    int updateSort(SortDto sortDto);

    int moveSortsBackward(SortDto sortDto);

    int moveSortsForward(SortDto sortDto);

    List<CourseDto> selectCourseByChoose(@Param("coursePageDto")CoursePageDto coursePageDto);
}
