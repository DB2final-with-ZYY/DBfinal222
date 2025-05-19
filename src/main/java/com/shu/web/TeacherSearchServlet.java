package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.TeacherSearchDTO;
import com.shu.mapper.TeacherMapper;
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

@WebServlet("/teacherSearch")
public class TeacherSearchServlet extends HttpServlet {

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

        String teacherIdStr = req.getParameter("teacherId");
        Integer teacherId = (teacherIdStr != null && !teacherIdStr.isEmpty()) ? Integer.parseInt(teacherIdStr) : null;

        String teacherName = encodeString(req.getParameter("teacherName"));
        String gender = encodeString(req.getParameter("gender"));
        String email = encodeString(req.getParameter("email"));
        String position = encodeString(req.getParameter("position"));
        String departmentIdStr = req.getParameter("department");
        Integer departmentId = (departmentIdStr != null && !departmentIdStr.isEmpty()) ? Integer.parseInt(departmentIdStr) : null;
        String status = encodeString(req.getParameter("status"));

        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        resp.setContentType("application/json");

        try {
            TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
            List<TeacherSearchDTO> results = teacherMapper.searchTeachers(
                teacherId, teacherName, gender, email, position, departmentId, status
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
