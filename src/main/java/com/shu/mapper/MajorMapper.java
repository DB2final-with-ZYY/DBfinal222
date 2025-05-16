package com.shu.mapper;

import com.shu.pojo.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MajorMapper {
    /**
     * 根据id查专业
     * @param MajorId
     * @return
     */
    Major selectMajorById(int MajorId);


    /**
     * 根据院系名称查询专业列表
     * @param departmentName 院系名称
     * @return 专业列表
     */
    List<Major> selectMajorsByDepartment(String departmentName);

    /**
     * 根据院系ID查询专业列表
     * @param departmentId 院系ID
     * @return 专业列表
     */
    List<Major> selectMajorsByDepartmentId(@Param("departmentId") int departmentId);

}