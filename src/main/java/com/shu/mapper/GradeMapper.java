package com.shu.mapper;

import com.shu.pojo.Grade;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeMapper {
    /**
     * 更新成绩 需要enrollment_id grade
     * @param grade
     * @return
     */
    int update(Grade grade);

    /**
     * 插入成绩 需要enrollment_id grade
     * @param grade
     * @return
     */
    int insert(Grade grade);
    
} 