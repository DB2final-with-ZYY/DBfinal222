package com.shu.mapper;

import com.shu.dto.CourseSearchDTO;
import com.shu.pojo.Enrollment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EnrollmentMapper {

    /**
     * 插入选课情况 要填充student_id 和schedule_id
     * 
     * @param enrollment
     * @return
     */
    int insert(Enrollment enrollment);

    /**
     * 获取某个课程的已选人数
     * 
     * @param scheduleId 课程安排ID
     * @return 已选人数
     */
    int getEnrolledCount(Integer scheduleId);

    /**
     * 获取学生已选课程的上课时间
     * 
     * @param studentId 学生ID
     * @return 已选课程的上课时间列表
     */
    List<String> getStudentClassTimes(Integer studentId);

    /**
     * 根据学生id查询已选课程的信息
     * @param studentId
     * @return
     */
    List<CourseSearchDTO> selectEnrolledCourses(Integer studentId);

}