package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.CourseSearchDTO;
import com.shu.mapper.CourseMapper;
import com.shu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/coursesSearch")
public class CourseSearchServlet extends HttpServlet {

    private String encodeString(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        // 获取搜索参数
        String courseIdStr = req.getParameter("courseId");
        Integer courseId = (courseIdStr != null && !courseIdStr.isEmpty()) ? Integer.parseInt(courseIdStr) : null;

        String courseName = encodeString(req.getParameter("courseName"));
        String creditStr = req.getParameter("credit");
        Integer credit = (creditStr != null && !creditStr.isEmpty()) ? Integer.parseInt(creditStr) : null;
        String departmentIdStr = req.getParameter("department");
        Integer departmentId = (departmentIdStr != null && !departmentIdStr.isEmpty()) ? Integer.parseInt(departmentIdStr) : null;
        String status = encodeString(req.getParameter("status"));

        // 打印调试信息
        System.out.println("Search parameters:");
        System.out.println("courseId: " + courseId);
        System.out.println("courseName: " + courseName);
        System.out.println("credit: " + credit);
        System.out.println("departmentId: " + departmentId);
        System.out.println("status: " + status);

        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        resp.setContentType("application/json");

        try {
            CourseMapper courseMapper = sqlSession.getMapper(CourseMapper.class);
            List<CourseSearchDTO> results = courseMapper.searchCourses(
                courseId, courseName, credit, departmentId, status
            );

            // 打印查询结果
            System.out.println("Query results size: " + (results != null ? results.size() : 0));

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(results);
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"查询失败，请稍后重试\"}");
        } finally {
            sqlSession.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
