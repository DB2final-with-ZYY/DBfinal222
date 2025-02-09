package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.mapper.EnrollmentMapper;
import com.shu.pojo.ClassSchedule;
import com.shu.pojo.Enrollment;
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
import java.util.Map;

@WebServlet("/removeStudent")
public class RemoveStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的教师信息
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取前端传来的课程信息
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestData = mapper.readValue(req.getReader(), Map.class);
        System.out.println(requestData.toString());

        Integer studentId = (Integer)requestData.get("studentId");
        Integer courseId = (Integer) requestData.get("courseId");
        Integer teacherId = (Integer) requestData.get("teacherId");
        String classTime = (String) requestData.get("classTime");


        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            // enrollment需要的信息：studenId scheduleId
            // scheduleId 用getClassScheduleByInfo的方法获取 需要 course_id teacher_id class_time
            // studenId course_id teacher_id class_time 我们都可以从前端传过来的时候获取

            // 1. 根据课程号、教师号和上课时间查询对应的课程安排（class_schedule表）
            ClassScheduleMapper classScheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);
            ClassSchedule classSchedule = classScheduleMapper.getClassScheduleByInfo(courseId, teacherId, classTime);

            if (classSchedule == null) {
                resp.getWriter().write("{\"success\": false, \"message\": \"未找到对应的课程\"}");
                return;
            }

            EnrollmentMapper enrollmentMapper = sqlSession.getMapper(EnrollmentMapper.class);

            // 5. 删除选课记录
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);
            enrollment.setScheduleId(classSchedule.getScheduleId());

            int result = enrollmentMapper.delete(enrollment);

            if (result > 0) {
                sqlSession.commit();
                resp.getWriter().write("{\"success\": true, \"message\": \"移除学生成功\"}");
            } else {
                resp.getWriter().write("{\"success\": false, \"message\": \"移除学生失败\"}");
            }

        } catch (Exception e) {
            sqlSession.rollback();
            resp.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
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
