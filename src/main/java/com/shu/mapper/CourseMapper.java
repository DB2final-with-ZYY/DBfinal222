package com.shu.mapper;

import com.shu.dto.CourseSearchDTO;
import com.shu.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * 根据条件搜索课程信息
     *
     * @param courseId      课程ID
     * @param courseName    课程名称
     * @param credit       学分
     * @param departmentId 院系ID
     * @param status       状态
     * @return 课程信息列表
     */
    List<CourseSearchDTO> searchCourses(
            @Param("courseId") Integer courseId,
            @Param("courseName") String courseName,
            @Param("credit") Integer credit,
            @Param("departmentId") Integer departmentId,
            @Param("status") String status
    );

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