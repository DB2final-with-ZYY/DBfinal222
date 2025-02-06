package com.shu.mapper;

import com.shu.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {
    /**
     * 查询学生用id 只返回一个
     * @param id
     * @return
     */
    Student selectById(Integer id);
}

