package com.shu.mapper;

import com.shu.dto.CourseSearchDTO;
import com.shu.pojo.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExamMapper {
    /**
     * 插入考试 需要schedule_id exam_time exam_place
     * @param exam
     * @return
     */
    int insert(Exam exam);

    /**
     * 修改考试 需要schedule_id exam_time exam_place
     * @param exam
     * @return
     */
    int update(Exam exam);

    /**
     * 根据条件搜索考试安排信息
     * @param examId        考试ID
     * @param scheduleId    课程安排ID
     * @param courseId      课程号
     * @param courseName    课程名称
     * @param teacherId     教师号
     * @param teacherName   教师姓名
     * @param examTime      考试时间
     * @param examPlace     考试地点
     * @return 考试安排信息列表
     */
    List<CourseSearchDTO> searchSchedules(
            @Param("examId") Integer examId,
            @Param("scheduleId") Integer scheduleId,
            @Param("courseId") Integer courseId,
            @Param("courseName") String courseName,
            @Param("teacherId") Integer teacherId,
            @Param("teacherName") String teacherName,
            @Param("examTime") String examTime,
            @Param("examPlace") String examPlace
    );
}
