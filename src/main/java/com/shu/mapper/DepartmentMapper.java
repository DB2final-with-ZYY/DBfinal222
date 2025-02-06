package com.shu.mapper;

import com.shu.pojo.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    /**
     * 查询所有院系
     * 
     * @return 院系列表
     */
    List<Department> selectAll();
}