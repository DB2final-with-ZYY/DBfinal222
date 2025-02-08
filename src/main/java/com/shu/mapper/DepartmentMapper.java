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

    /**
     * 根据id查院系
     * @param departmentId
     * @return
     */
    Department selectById(Integer departmentId);

    /**
     *  根据名字查学院
     * @param departmentName
     * @return
     */
    Department selectByName(String departmentName);
}