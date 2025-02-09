package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.CourseSearchDTO;
import com.shu.mapper.ClassScheduleMapper;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/examArrange")
public class ExamArrangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 设置响应类型
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的教师信息
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 读取 JSON 数据
        BufferedReader reader = req.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        String jsonData = jsonBuilder.toString();
        if (jsonData.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"请求体为空\"}");
            return;
        }

        // 解析 JSON 数据
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> data = objectMapper.readValue(jsonData, Map.class);

        String scheduleIdStr = data.get("scheduleId");
        String examTime = data.get("examTime");
        String examPlace = data.get("examPlace");

        if (scheduleIdStr == null || examTime == null || examPlace == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"参数不能为空\"}");
            return;
        }

        int scheduleId = Integer.parseInt(scheduleIdStr);
        try {
            scheduleId = Integer.parseInt(scheduleIdStr);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"数值字段格式错误\"}");
            return;
        }

        System.out.println("scheduleId:" + scheduleId);
        System.out.println("examTime:" + examTime);
        System.out.println("examPlace:" + examPlace);

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"服务器内部错误\"}");
        } finally {
            // 关闭SqlSession
            sqlSession.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
}
