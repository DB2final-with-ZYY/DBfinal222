package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.mapper.TeacherMapper;
import com.shu.dto.CourseSearchDTO;
import com.shu.dto.TeacherSearchDTO;
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
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/adminTeacherSchedule")
public class AdminTeacherScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应类型
        response.setContentType("application/json;charset=utf-8");

        // 获取教师ID参数
        String teacherIdStr = request.getParameter("teacherId");
        Integer teacherId = null;
        if (teacherIdStr != null && !teacherIdStr.isEmpty()) {
            teacherId = Integer.parseInt(teacherIdStr);
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            ClassScheduleMapper classScheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);

            // 获取教师课程信息
            if (teacherId != null) {
                // 如果指定了教师ID，只获取该教师的课程
                List<CourseSearchDTO> teacherCourses = classScheduleMapper.selectClassScheduleByTeacherId(teacherId);
                // 直接返回课程列表
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), teacherCourses);
            } else {
                // 如果没有指定教师ID，获取所有教师的课程信息
                List<CourseSearchDTO> teacherCourses = classScheduleMapper.selectTeacherCourses();
                // 按教师ID分组
                Map<Integer, List<CourseSearchDTO>> teacherMap = teacherCourses.stream()
                        .collect(Collectors.groupingBy(CourseSearchDTO::getTeacherId));
                // 转换为JSON并响应
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), teacherMap);
            }

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
