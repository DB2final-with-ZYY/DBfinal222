package com.shu.mapper;

import com.shu.pojo.Enrollment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnrollmentMapper {

    /**
     * 插入选课情况 要填充student_id 和schedule_id
     * @param enrollment
     * @return
     */
    int insert(Enrollment enrollment);
    
} 