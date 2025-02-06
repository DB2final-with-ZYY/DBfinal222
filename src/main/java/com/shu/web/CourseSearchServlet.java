package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.CourseSearchDTO;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.mapper.StudentMapper;
import com.shu.mapper.TeacherMapper;
import com.shu.pojo.Student;
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
import java.util.List;
import java.nio.charset.StandardCharsets;

@WebServlet("/courseSearch")
public class CourseSearchServlet extends HttpServlet {

    // 添加编码转换方法
    private String encodeString(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 1.接收检索条件
        String courseIdStr = req.getParameter("courseId");
        Integer courseId = (courseIdStr != null && !courseIdStr.isEmpty()) ? Integer.parseInt(courseIdStr) : null;

        String courseName = encodeString(req.getParameter("courseName"));

        String teacherIdStr = req.getParameter("teacherId");
        Integer teacherId = (teacherIdStr != null && !teacherIdStr.isEmpty()) ? Integer.parseInt(teacherIdStr) : null;

        String teacherName = encodeString(req.getParameter("teacherName"));
        String department = encodeString(req.getParameter("department"));

        String creditStr = req.getParameter("credit");
        Integer credit = (creditStr != null && !creditStr.isEmpty()) ? Integer.parseInt(creditStr) : null;

        String weekday = encodeString(req.getParameter("weekday"));

        String timeSlot = req.getParameter("timeSlot");

        String capacityMinStr = req.getParameter("capacityMin");
        Integer capacityMin = (capacityMinStr != null && !capacityMinStr.isEmpty()) ? Integer.parseInt(capacityMinStr)
                : null;

        String capacityMaxStr = req.getParameter("capacityMax");
        Integer capacityMax = (capacityMaxStr != null && !capacityMaxStr.isEmpty()) ? Integer.parseInt(capacityMaxStr)
                : null;

        Boolean hasSpace = Boolean.valueOf(req.getParameter("hasSpace"));

        // 打印接收到的参数信息
        System.out.println("CourseSearchServlet:");
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Teacher ID: " + teacherId);
        System.out.println("Teacher Name: " + teacherName);
        System.out.println("Department: " + department);
        System.out.println("Credit: " + credit);
        System.out.println("Weekday: " + weekday);
        System.out.println("Time Slot: " + timeSlot);
        System.out.println("Capacity Min: " + capacityMin);
        System.out.println("Capacity Max: " + capacityMax);
        System.out.println("Has Space: " + hasSpace);

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 获取字符输出流,并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        // 2.3 获取Mapper
        ClassScheduleMapper classScheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);
        // 2.4 调用方法
        List<CourseSearchDTO> results = classScheduleMapper.searchCourses(courseId, courseName, teacherId, teacherName,
                department, credit, weekday, timeSlot, capacityMin, capacityMax, hasSpace);

        // 设置响应类型和字符编码
        resp.setContentType("application/json");

        try {
            // 创建ObjectMapper对象用于JSON转换
            ObjectMapper objectMapper = new ObjectMapper();

            // 将查询结果转换为JSON并写入响应
            String jsonResponse = objectMapper.writeValueAsString(results);
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            // 返回错误信息
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"查询失败，请稍后重试\"}");
        }

        // 2.5 释放资源
        sqlSession.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
