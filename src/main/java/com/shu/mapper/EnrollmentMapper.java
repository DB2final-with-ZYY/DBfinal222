package com.shu.mapper;

import com.shu.pojo.Enrollment;
import org.apache.ibatis.annotations.Mapper;

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
}