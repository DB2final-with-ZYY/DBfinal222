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
import java.io.PrintWriter;
import java.math.BigDecimal;
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
        String departmentId = req.getParameter("department");
        String credit = req.getParameter("credit");
        String examWeight = req.getParameter("examWeight");

        System.out.println("/createCourse");
        System.out.println("courseName: " + courseName);
        System.out.println("departmentId: " + departmentId);
        System.out.println("credit: " + credit);
        System.out.println("examWeight: " + examWeight);


        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
//            // 查学院id
//            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
//            Department department = departmentMapper.selectByName(departmentName);

            // 封装
            Course course = new Course();
            course.setCourseName(courseName);
            course.setCredit(Integer.parseInt(credit));
            course.setDepartmentId(Integer.parseInt(departmentId));
            course.setExamWeight(new BigDecimal(examWeight));

            // 插入
            CourseMapper courseMapper = sqlSession.getMapper(CourseMapper.class);
            int count = courseMapper.insert(course);

            // 获取字符输出流,并设置content type
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter writer = resp.getWriter();

            if (count > 0) {
                // 提交
                sqlSession.commit();
                writer.write("<html><body>");
                writer.write("<h1>创建成功！</h1>");
                writer.write("<script>window.alert(\"创建成功,请联系管理审核\");</script>");
                writer.write("<script>window.location.href='createSchedule.html';</script>");
                writer.write("</body></html>");
            } else {
                // 失败，弹出提示框
                sqlSession.rollback();
                writer.write("<html><body>");
                writer.write("<h1>创建失败！</h1>");
                writer.write("<script>alert('创建失败！'); window.history.back();</script>"); // 弹窗并返回上一步
                writer.write("</body></html>");
            }

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
