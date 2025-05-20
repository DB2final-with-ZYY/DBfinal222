package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.CourseMapper;
import com.shu.pojo.Course;
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

@WebServlet("/addCourse")
public class AddCourseServlet extends HttpServlet {
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
        Course course = objectMapper.readValue(request.getReader(), Course.class);

        // 验证必填字段
        if (course.getCourseName() == null || course.getDepartmentId() == null ||
            course.getCredit() == null || course.getExamWeight() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "必填字段不能为空");
            objectMapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // 设置默认状态
        if (course.getStatus() == null) {
            course.setStatus("待审核");
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取CourseMapper
            CourseMapper courseMapper = sqlSession.getMapper(CourseMapper.class);

            // 插入课程信息
            int result = courseMapper.insert(course);

            // 提交事务
            sqlSession.commit();

            // 准备响应数据
            Map<String, Object> responseData = new HashMap<>();
            if (result > 0) {
                responseData.put("success", true);
                responseData.put("message", "新增课程成功");
            } else {
                responseData.put("success", false);
                responseData.put("message", "新增课程失败");
            }

            // 发送响应
            objectMapper.writeValue(response.getWriter(), responseData);
        } catch (Exception e) {
            // 发生异常时返回错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "新增课程时发生错误: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        this.doPost(request, response);
    }
}
