package com.shu.mapper;

import com.shu.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper {
    /**
     * id查teacher
     * @param id
     * @return
     */
    Teacher selectById(Integer id);
    
} 