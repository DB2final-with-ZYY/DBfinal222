package com.shu.mapper;

import com.shu.dto.StudentSearchDTO;
import com.shu.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    /**
     * 根据条件搜索学生信息
     *
     * @param studentId      学生ID
     * @param studentName    学生姓名
     * @param gender        性别
     * @param email         邮箱
     * @param gradeNumber   年级
     * @param nativePlace   籍贯
     * @param departmentId  院系ID
     * @param majorId       专业ID
     * @param status        学籍状态
     * @return 学生信息列表
     */
    List<StudentSearchDTO> searchStudents(
            @Param("studentId") Integer studentId,
            @Param("studentName") String studentName,
            @Param("gender") String gender,
            @Param("email") String email,
            @Param("gradeNumber") Integer gradeNumber,
            @Param("nativePlace") String nativePlace,
            @Param("departmentId") Integer departmentId,
            @Param("majorId") Integer majorId,
            @Param("status") String status
    );


    /**
     * 查询学生用id 只返回一个
     * @param id
     * @return
     */
    Student selectById(Integer id);

    /**
     * 插入学生信息
     * @param student 学生信息
     * @return 影响行数
     */
    int insert(Student student);

    /**
     * 更新学生信息
     * @param student 学生信息
     * @return 影响行数
     */
    int update(Student student);

    /**
     * 删除学生信息
     * @param studentId 学生ID
     * @return 影响行数
     */
    int delete(@Param("studentId") Integer studentId);

}

