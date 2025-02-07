package com.shu.web;

import com.shu.mapper.StudentMapper;
import com.shu.mapper.TeacherMapper;
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
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 获取分类：学生还是教师
        String role = req.getParameter("role");

        // 1.接收用户名和密码
        String id = req.getParameter("id");
        String password = req.getParameter("password");

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 获取字符输出流,并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();


        if (role.equals("student")) {
            // 2.3 获取Mapper
            StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
            // 2.4 调用方法
            Student student = studentMapper.selectById(Integer.parseInt(id));

            if (student.getPassword().equals(password)) {
                // 登陆成功，跳转到主页或其他页面
                writer.write("<html><body>");
                writer.write("<h1>登录成功！</h1>");
                writer.write("<script>window.alert(\"登陆成功\");</script>");
                writer.write("<script>window.location.href='courseSelect.html';</script>"); // 登录成功后跳转
                writer.write("</body></html>");

                // 存入session
                HttpSession session = req.getSession();
                session.setAttribute("student", student);
            } else {
                // 登陆失败，弹出提示框
                writer.write("<html><body>");
                writer.write("<h1>登录失败！</h1>");
                writer.write("<script>alert('Id或密码错误！'); window.history.back();</script>"); // 弹窗并返回上一步
                writer.write("</body></html>");
            }


        }
        else if (role.equals("teacher")) {
            // 2.3 获取Mapper
            TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
            // 2.4 调用方法
            Teacher teahcer = teacherMapper.selectById(Integer.parseInt(id));
            if (teahcer.getPassword().equals(password)) {
                // 登陆成功，跳转到主页或其他页面
                writer.write("<html><body>");
                writer.write("<h1>登录成功！</h1>");
                writer.write("<script>window.alert(\"登陆成功\");</script>");
                writer.write("<script>window.location.href='teacherCourseSearch.html';</script>"); // 登录成功后跳转
                writer.write("</body></html>");

                // 存入session
                HttpSession session = req.getSession();
                session.setAttribute("teacher", teahcer);

            } else {
                // 登陆失败，弹出提示框
                writer.write("<html><body>");
                writer.write("<h1>登录失败！</h1>");
                writer.write("<script>alert('Id或密码错误！'); window.history.back();</script>"); // 弹窗并返回上一步
                writer.write("</body></html>");
            }
        }

        // 2.5 释放资源
        sqlSession.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
