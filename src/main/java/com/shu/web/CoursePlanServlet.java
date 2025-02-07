package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.CourseSearchDTO;
import com.shu.mapper.CoursePlanMapper;
import com.shu.mapper.EnrollmentMapper;
import com.shu.mapper.StudentMapper;
import com.shu.pojo.Course;
import com.shu.pojo.Student;
import com.shu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/coursePlan")
public class CoursePlanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的学生
        HttpSession session = req.getSession();
        Student student = (Student) session.getAttribute("student");

        if (student == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            // 获取学生完整信息
            StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
            student = studentMapper.selectById(student.getStudentId());

            // 查询该学生所在专业的选课方案
            CoursePlanMapper coursePlanMapper = sqlSession.getMapper(CoursePlanMapper.class);
            List<Course> courses = coursePlanMapper.searchCoursePlanByMajor(student.getMajorId());

            // 创建ObjectMapper对象用于JSON转换
            ObjectMapper objectMapper = new ObjectMapper();

            // 将查询结果转换为JSON并写入响应
            String jsonResponse = objectMapper.writeValueAsString(courses);
            resp.getWriter().write(jsonResponse);

        } finally {
            sqlSession.close();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
