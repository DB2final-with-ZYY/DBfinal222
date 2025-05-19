package com.shu.mapper;

import com.shu.dto.TeacherSearchDTO;
import com.shu.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherMapper {
    /**
     * id查teacher
     * @param id
     * @return
     */
    Teacher selectById(Integer id);

    /**
     * 插入教师信息
     * @param teacher 教师信息
     * @return 影响行数
     */
    int insert(Teacher teacher);

    /**
     * 更新教师信息
     * @param teacher 教师信息
     * @return 影响行数
     */
    int update(Teacher teacher);

    /**
     * 根据条件搜索教师信息
     *
     * @param teacherId      教师ID
     * @param teacherName    教师姓名
     * @param gender        性别
     * @param email         邮箱
     * @param position      职位
     * @param departmentId  院系ID
     * @param status        状态
     * @return 教师信息列表
     */
    List<TeacherSearchDTO> searchTeachers(
            @Param("teacherId") Integer teacherId,
            @Param("teacherName") String teacherName,
            @Param("gender") String gender,
            @Param("email") String email,
            @Param("position") String position,
            @Param("departmentId") Integer departmentId,
            @Param("status") String status
    );
} 