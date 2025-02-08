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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/createSchedule")
public class CreateScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的教师ID
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取信息
        String courseId = req.getParameter("courseId");
        String courseName = req.getParameter("courseName");
        String credit = req.getParameter("credit");
        String classroom = req.getParameter("classroom");
        String capacity = req.getParameter("capacity");
        String semester = req.getParameter("semester");
        String classTime = req.getParameter("classTime");

        // 封装信息
        ClassSchedule classSchedule = new ClassSchedule();
        classSchedule.setSemester(Integer.parseInt(semester));
        classSchedule.setCourseId(Integer.parseInt(courseId));
        classSchedule.setTeacherId(teacher.getTeacherId());
        classSchedule.setClassroom(classroom);
        classSchedule.setCapacity(Integer.parseInt(capacity));
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
                writer.write("<html><body>");
                writer.write("<h1>创建成功！</h1>");
                writer.write("<script>window.alert(\"创建成功\");</script>");
                writer.write("<script>window.location.href='teacherSchedule.html';</script>");
                writer.write("</body></html>");
            } else {
                // 失败，弹出提示框
                sqlSession.rollback();
                writer.write("<html><body>");
                writer.write("<h1>创建失败！</h1>");
                writer.write("<script>alert('创建失败！'); window.history.back();</script>"); // 弹窗并返回上一步
                writer.write("</body></html>");
            }


        } catch (Exception e) {
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
