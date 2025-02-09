package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.mapper.CourseMapper;
import com.shu.mapper.TeacherMapper;
import com.shu.dto.CourseSearchDTO;
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
import java.util.List;

@WebServlet("/teacherCourses")
public class TeacherScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应类型
        response.setContentType("application/json;charset=utf-8");

        // 获取当前登录的教师信息
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            ClassScheduleMapper classScheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);

            // 查询该教师的所有课程安排
            List<CourseSearchDTO> teacherCourses = classScheduleMapper
                    .selectClassScheduleByTeacherId(teacher.getTeacherId());
            // System.out.println(teacherCourses);

            // 转换为JSON并响应
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), teacherCourses);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"服务器内部错误\"}");
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
