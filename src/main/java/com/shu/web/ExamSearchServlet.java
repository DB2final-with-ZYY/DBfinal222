package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.CourseSearchDTO;
import com.shu.mapper.ExamMapper;
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

@WebServlet("/examSearch")
public class ExamSearchServlet extends HttpServlet {

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

        String examIdStr = req.getParameter("examId");
        Integer examId = (examIdStr != null && !examIdStr.isEmpty()) ? Integer.parseInt(examIdStr) : null;

        String scheduleIdStr = req.getParameter("scheduleId");
        Integer scheduleId = (scheduleIdStr != null && !scheduleIdStr.isEmpty()) ? Integer.parseInt(scheduleIdStr) : null;

        String courseIdStr = req.getParameter("courseId");
        Integer courseId = (courseIdStr != null && !courseIdStr.isEmpty()) ? Integer.parseInt(courseIdStr) : null;

        String courseName = encodeString(req.getParameter("courseName"));
        String teacherIdStr = req.getParameter("teacherId");
        Integer teacherId = (teacherIdStr != null && !teacherIdStr.isEmpty()) ? Integer.parseInt(teacherIdStr) : null;
        String teacherName = encodeString(req.getParameter("teacherName"));
        String examTime = encodeString(req.getParameter("examTime"));
        String examPlace = encodeString(req.getParameter("examPlace"));

        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            ExamMapper examMapper = sqlSession.getMapper(ExamMapper.class);
            List<CourseSearchDTO> results = examMapper.searchSchedules(
                examId, scheduleId, courseId, courseName, teacherId, teacherName, examTime, examPlace
            );

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
