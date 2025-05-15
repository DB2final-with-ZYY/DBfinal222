package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.StudentSearchDTO;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.mapper.EnrollmentMapper;
import com.shu.pojo.ClassSchedule;
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

@WebServlet("/classStudents")
public class ClassStudentsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应类型
        resp.setContentType("application/json;charset=utf-8");
        req.setCharacterEncoding("utf-8");

        // 获取当前登录的教师信息
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 读取请求体中的JSON数据
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // 解析JSON数据
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> scheduleData = mapper.readValue(sb.toString(), Map.class);

        // 打印接收到的数据
//        System.out.println("Received data: " + sb.toString());
//        System.out.println("Parsed data: " + scheduleData);

        // 添加空值检查
        if (!scheduleData.containsKey("courseId") || !scheduleData.containsKey("teacherId") || !scheduleData.containsKey("classTime")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"缺少必要参数\"}");
            return;
        }

        // 获取并转换数据
        Integer courseId = Integer.valueOf(scheduleData.get("courseId").toString());
        Integer teacherId = Integer.valueOf(scheduleData.get("teacherId").toString());
        String classTime = scheduleData.get("classTime").toString();

        // 验证数据
        if (courseId == null || teacherId == null || classTime == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"参数不完整\"}");
            return;
        }

//        System.out.println(courseId + " " + teacherId + " " + classTime);

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            ClassScheduleMapper classScheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);
            ClassSchedule schedule = classScheduleMapper.getClassScheduleByInfo(courseId, teacherId, classTime);
//            System.out.println("(ClassStudentServlet)scheduleId:" + schedule.getScheduleId());

            EnrollmentMapper enrollmentMapper = sqlSession.getMapper(EnrollmentMapper.class);
            List<StudentSearchDTO> studentSearchDTOS = enrollmentMapper.selectStudentsByScheduleId(schedule.getScheduleId());
            System.out.println("(ClassStudentsServlet)studentSearchDTOS:" + studentSearchDTOS);

            // 传给前端
            // 转换为JSON并响应
//            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(), studentSearchDTOS);


        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"服务器内部错误\"}");
        } finally {
            sqlSession.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
