package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.DepartmentMapper;
import com.shu.mapper.MajorMapper;
import com.shu.mapper.StudentMapper;
import com.shu.pojo.Department;
import com.shu.pojo.Major;
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
import java.util.Map;

@WebServlet("/studentInfo")
public class StudentInformationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的学生ID
        HttpSession session = req.getSession();
        Student student = (Student) session.getAttribute("student");

        if (student == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            // 获取StudentMapper
            StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);

            // 查询学生信息
            student = studentMapper.selectById(student.getStudentId());

            if (student == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"学生信息不存在\"}");
                return;
            }
            System.out.println("Studentinformation:" + student);

            // 获取学院和专业mapper
            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
            MajorMapper majorMapper = sqlSession.getMapper(MajorMapper.class);

            // 查学院和专业名称
            Department department = departmentMapper.selectById(student.getDepartmentId());
            Major major = majorMapper.selectMajorById(student.getMajorId());

            // 创建返回的数据对象
            Map<String, Object> data = new HashMap<>();
            data.put("studentId", student.getStudentId());
            data.put("name", student.getName());
            data.put("gender", student.getGender().equals("M") ? "男" : student.getGender().equals("F") ? "女" : "其他");
            data.put("gradeNumber", student.getGradeNumber());
            data.put("email", student.getEmail());
            data.put("nativePlace", student.getNativePlace());
            data.put("departmentName", department.getDepartmentName());
            data.put("majorName", major.getMajorName());
            data.put("status", student.getStatus());

            // 转换为JSON并响应
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(), data);

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
