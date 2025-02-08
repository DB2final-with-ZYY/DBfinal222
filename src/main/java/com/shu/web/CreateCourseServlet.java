package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.CourseMapper;
import com.shu.mapper.DepartmentMapper;
import com.shu.mapper.TeacherMapper;
import com.shu.pojo.Course;
import com.shu.pojo.Department;
import com.shu.pojo.Teacher;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/createCourse")
public class CreateCourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的教师ID
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取课程信息
        String courseName = req.getParameter("courseName");
        String departmentName = req.getParameter("departmentName");
        String credit = req.getParameter("credit");

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            // 查学院id
            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
            Department department = departmentMapper.selectByName(departmentName);

            // 封装
            Course course = new Course();
            course.setCourseName(courseName);
            course.setCredit(Integer.parseInt(credit));
            course.setDepartmentId(department.getDepartmentId());

            // 插入
            CourseMapper courseMapper = sqlSession.getMapper(CourseMapper.class);
            courseMapper.insert(course);

            // 提交
            sqlSession.commit();
        } finally {
            // 释放资源
            sqlSession.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
