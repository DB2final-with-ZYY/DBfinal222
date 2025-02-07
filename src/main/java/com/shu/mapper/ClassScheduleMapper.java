package com.shu.mapper;

import com.shu.dto.CourseSearchDTO;
import com.shu.pojo.ClassSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ClassScheduleMapper {
    
    /**
     * 根据条件搜索课程信息
     * @param courseId 课程ID
     * @param courseName 课程名称
     * @param teacherId 教师ID
     * @param teacherName 教师姓名
     * @param departmentName 开课学院
     * @param credit 学分
     * @param weekday 星期几
     * @param timeSlot 第几节
     * @param capacityMin 最小容量
     * @param capacityMax 最大容量
     * @param hasSpace 是否只显示未满课程
     * @return 课程信息列表
     */
    List<CourseSearchDTO> searchCourses(@Param("courseId") Integer courseId,
                                      @Param("courseName") String courseName,
                                      @Param("teacherId") Integer teacherId,
                                      @Param("teacherName") String teacherName,
                                      @Param("departmentName") String departmentName,
                                      @Param("credit") Integer credit,
                                      @Param("weekday") String weekday,
                                      @Param("timeSlot") String timeSlot,
                                      @Param("capacityMin") Integer capacityMin,
                                      @Param("capacityMax") Integer capacityMax,
                                      @Param("hasSpace") Boolean hasSpace);

    /**
     * 用在选课时 确定课程号教师号和时间就能确定唯一的课
     * @param courseId
     * @param teacherId
     * @param classTime
     * @return
     */
    ClassSchedule getClassScheduleByInfo(@Param("courseId") Integer courseId,
                                         @Param("teacherId") Integer teacherId,
                                         @Param("classTime") String classTime);

    /**
     * 查询教师的开课安排
     * @param teacherId
     * @return
     */
    List<ClassSchedule> selectClassScheduleByTeacherId(@Param("teacherId") Integer teacherId);
} 