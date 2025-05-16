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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/updateStudent")
public class UpdateStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // System.out.println("updateStudentSerlvet");

        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // 读取请求体
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // 解析JSON数据
            Student student = objectMapper.readValue(json.toString(), Student.class);

            // 获取SqlSession
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                // 获取StudentMapper
                StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);

                // 更新学生信息
                int rowsAffected = studentMapper.update(student);
                
                if (rowsAffected > 0) {
                    sqlSession.commit();
                    result.put("success", true);
                } else {
                    result.put("success", false);
                    result.put("message", "更新失败：未找到对应的学生记录");
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "服务器错误：" + e.getMessage());
        }

        // 发送响应
        objectMapper.writeValue(response.getWriter(), result);
    }
} 