package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dto.CourseSearchDTO;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.mapper.EnrollmentMapper;
import com.shu.pojo.ClassSchedule;
import com.shu.pojo.Enrollment;
import com.shu.pojo.Student;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/selectCourse")
public class EnrollmentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的学生
        HttpSession session = req.getSession();
        Student student = (Student) session.getAttribute("student");

        if (student == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取前端传来的课程信息
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestData = mapper.readValue(req.getReader(), Map.class);
        Integer courseId = (Integer) requestData.get("courseId");
        Integer teacherId = (Integer) requestData.get("teacherId");
        String classTime = (String) requestData.get("classTime");
//        System.out.println(requestData.toString());

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

            // 2. 检查课程容量
            int capacity = classSchedule.getCapacity();
            EnrollmentMapper enrollmentMapper = sqlSession.getMapper(EnrollmentMapper.class);
            int enrolledCount = enrollmentMapper.getEnrolledCount(classSchedule.getScheduleId());

            if (enrolledCount >= capacity) {
                resp.getWriter().write("{\"success\": false, \"message\": \"课程已满\"}");
                return;
            }

            // 3. 检查时间冲突
            List<String> studentClassTimes = enrollmentMapper.getStudentClassTimes(student.getStudentId());
            String newClassTime = classSchedule.getClassTime();

            // 解析新课程的时间
            String newWeekday = newClassTime.substring(0, 3); // 获取"星期x"
            String newTimeSlot = newClassTime.substring(3); // 获取节次，如"1-2"

            for (String existingTime : studentClassTimes) {
                // 解析已选课程的时间
                String existingWeekday = existingTime.substring(0, 3);
                String existingTimeSlot = existingTime.substring(3);

                // 如果星期相同且时间段相同，则存在冲突
                if (newWeekday.equals(existingWeekday) && newTimeSlot.equals(existingTimeSlot)) {
                    resp.getWriter().write("{\"success\": false, \"message\": \"与已选课程时间冲突\"}");
                    return;
                }
            }

            // 4. 检查是不是选过的课程号
            List<CourseSearchDTO> courseSearchDTOS = enrollmentMapper.selectEnrolledCourses(student.getStudentId());
            for (CourseSearchDTO courseSearchDTO : courseSearchDTOS) {
                if(courseSearchDTO.getCourseId().equals(courseId)){
                    resp.getWriter().write("{\"success\": false, \"message\": \"已选过该课程号课程\"}");
                    return;
                }
            }

            // 5. 插入选课记录
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(student.getStudentId());
            enrollment.setScheduleId(classSchedule.getScheduleId());

            int result = enrollmentMapper.insert(enrollment);

            if (result > 0) {
                sqlSession.commit();
                resp.getWriter().write("{\"success\": true, \"message\": \"选课成功\"}");
            } else {
                resp.getWriter().write("{\"success\": false, \"message\": \"选课失败\"}");
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
