package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.StudentMapper;
import com.shu.pojo.Student;
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

@WebServlet("/addStudent")
public class AddStudentServlet extends HttpServlet {
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
        Student student = objectMapper.readValue(request.getReader(), Student.class);

        // 验证必填字段
        if (student.getName() == null || student.getEmail() == null ||
            student.getGradeNumber() == null || student.getDepartmentId() == null || student.getMajorId() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "必填字段不能为空");
            objectMapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // 设置默认密码（学号后6位）
        String defaultPassword = String.format("%06d", student.getStudentId());
        student.setPassword(defaultPassword);

        // 设置默认状态
        if (student.getStatus() == null) {
            student.setStatus("正常");
        }

        // 设置默认性别
        if (student.getGender() == null) {
            student.setGender("O");
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取StudentMapper
            StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);

//            // 检查学号是否已存在
//            Student existingStudent = studentMapper.selectById(student.getStudentId());
//            if (existingStudent != null) {
//                Map<String, Object> errorResponse = new HashMap<>();
//                errorResponse.put("success", false);
//                errorResponse.put("message", "学号已存在，请使用其他学号");
//                objectMapper.writeValue(response.getWriter(), errorResponse);
//                return;
//            }

            // 插入学生信息
            int result = studentMapper.insert(student);

            // 提交事务
            sqlSession.commit();

            // 准备响应数据
            Map<String, Object> responseData = new HashMap<>();
            if (result > 0) {
                responseData.put("success", true);
                responseData.put("message", "新增学生成功");
            } else {
                responseData.put("success", false);
                responseData.put("message", "新增学生失败");
            }

            // 发送响应
            objectMapper.writeValue(response.getWriter(), responseData);
        } catch (Exception e) {
            // 发生异常时返回错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "新增学生时发生错误: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
} 