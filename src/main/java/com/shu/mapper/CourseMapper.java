package com.shu.mapper;

import com.shu.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * 查所在学院课程
     * @param departmentId
     * @return
     */
    List<Course> selectByDepartment(Integer departmentId);

    /**
     * 插入课程
     * @param course
     * @return
     */
    int insert(Course course);
    
} 