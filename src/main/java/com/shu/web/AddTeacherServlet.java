package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.TeacherMapper;
import com.shu.pojo.Teacher;
import com.shu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/addTeacher")
public class AddTeacherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 读取请求体中的JSON数据
        Teacher teacher = objectMapper.readValue(request.getReader(), Teacher.class);

        // 验证必填字段
        if (teacher.getName() == null || teacher.getEmail() == null ||
            teacher.getDepartmentId() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "必填字段不能为空");
            objectMapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // 设置默认密码（工号后6位）
        String defaultPassword = String.format("%06d", teacher.getTeacherId());
        teacher.setPassword(defaultPassword);

        // 设置默认状态
        if (teacher.getStatus() == null) {
            teacher.setStatus("在职");
        }

        // 设置默认性别
        if (teacher.getGender() == null) {
            teacher.setGender("O");
        }

        // 设置默认职位
        if (teacher.getPosition() == null) {
            teacher.setPosition("讲师");
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取TeacherMapper
            TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);

            // 检查工号是否已存在
            Teacher existingTeacher = teacherMapper.selectById(teacher.getTeacherId());
            if (existingTeacher != null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "工号已存在，请使用其他工号");
                objectMapper.writeValue(response.getWriter(), errorResponse);
                return;
            }

            // 插入教师信息
            int result = teacherMapper.insert(teacher);

            // 提交事务
            sqlSession.commit();

            // 准备响应数据
            Map<String, Object> responseData = new HashMap<>();
            if (result > 0) {
                responseData.put("success", true);
                responseData.put("message", "新增教师成功");
            } else {
                responseData.put("success", false);
                responseData.put("message", "新增教师失败");
            }

            // 发送响应
            objectMapper.writeValue(response.getWriter(), responseData);
        } catch (Exception e) {
            // 发生异常时返回错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "新增教师时发生错误: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}
