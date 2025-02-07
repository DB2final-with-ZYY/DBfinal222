package com.shu.mapper;

import com.shu.dto.CourseSearchDTO;
import com.shu.pojo.Course;
import com.shu.pojo.CoursePlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoursePlanMapper {

    /**
     * 根据专业id查课程信息
     * @param majorId
     * @return
     */
    List<Course> searchCoursePlanByMajor(@Param("majorId") Integer majorId);
    
} 