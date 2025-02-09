package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.ClassScheduleMapper;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/deleteSchedule")
public class DeleteScheduleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        // 获取当前登录的教师
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 读取请求体中的JSON数据
        BufferedReader reader = request.getReader();
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
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"缺少必要参数\"}");
            return;
        }

        // 获取并转换数据
        Integer courseId = Integer.valueOf(scheduleData.get("courseId").toString());
        Integer teacherId = Integer.valueOf(scheduleData.get("teacherId").toString());
        String classTime = scheduleData.get("classTime").toString();

        // 验证数据
        if (courseId == null || teacherId == null || classTime == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"参数不完整\"}");
            return;
        }

        // 验证是否是当前教师的课程
        if (!teacherId.equals(teacher.getTeacherId())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"无权删除其他教师的课程安排\"}");
            return;
        }

        // 封装数据
        ClassSchedule classSchedule = new ClassSchedule();
        classSchedule.setClassTime(classTime);
        classSchedule.setTeacherId(teacherId);
        classSchedule.setCourseId(courseId);

        System.out.println(classSchedule);

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            // 获取ClassScheduleMapper
            ClassScheduleMapper classScheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);

            // 删除课程安排
            int result = classScheduleMapper.deleteClassSchedule(classSchedule);

            // 提交事务
            sqlSession.commit();

            // 构造响应
            Map<String, Object> response_data = new HashMap<>();
            if (result > 0) {
                response_data.put("success", true);
            } else {
                response_data.put("success", false);
                response_data.put("error", "删除失败，课程安排不存在");
            }

            // 发送响应
            mapper.writeValue(response.getWriter(), response_data);

        } catch (Exception e) {
            // 发生异常时回滚
            sqlSession.rollback();
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"服务器内部错误\"}");
        } finally {
            // 关闭SqlSession
            sqlSession.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }
}
