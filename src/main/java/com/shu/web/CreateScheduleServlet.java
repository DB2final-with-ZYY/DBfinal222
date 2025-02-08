package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.mapper.CourseMapper;
import com.shu.mapper.TeacherMapper;
import com.shu.pojo.ClassSchedule;
import com.shu.pojo.Course;
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
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/createSchedule")
public class CreateScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");

        // 获取当前登录的教师ID
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

        String courseIdStr = data.get("courseId");
        String classroom = data.get("classroom");
        String capacityStr = data.get("capacity");
        String semesterStr = data.get("semester");
        String classTime = data.get("classTime");

        if (courseIdStr == null || classroom == null || capacityStr == null || semesterStr == null || classTime == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"参数不能为空\"}");
            return;
        }

        int courseId, capacity, semester;
        try {
            courseId = Integer.parseInt(courseIdStr);
            capacity = Integer.parseInt(capacityStr);
            semester = Integer.parseInt(semesterStr);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"数值字段格式错误\"}");
            return;
        }

        // 封装课程信息
        ClassSchedule classSchedule = new ClassSchedule();
        classSchedule.setSemester(semester);
        classSchedule.setCourseId(courseId);
        classSchedule.setTeacherId(teacher.getTeacherId());
        classSchedule.setClassroom(classroom);
        classSchedule.setCapacity(capacity);
        classSchedule.setClassTime(classTime);

        System.out.println(classSchedule);

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
//            // 获取老师信息
//            TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
//            teacher = teacherMapper.selectById(teacher.getTeacherId());

            ClassScheduleMapper classScheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);
            int count = classScheduleMapper.insertClassSchedule(classSchedule);

            // 获取字符输出流,并设置content type
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter writer = resp.getWriter();

            if (count > 0) {
                // 提交
                sqlSession.commit();
                // 给前端响应
                resp.getWriter().write("{\"success\": true}");
            } else {
                // 失败，弹出提示框
                sqlSession.rollback();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"课程创建失败，请稍后重试\"}");
            }
        } catch (Exception e) {
            sqlSession.rollback();
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"服务器内部错误\"}");
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
