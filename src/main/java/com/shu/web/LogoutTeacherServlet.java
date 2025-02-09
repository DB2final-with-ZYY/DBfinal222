package com.shu.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/logoutTeacher")
public class LogoutTeacherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 获取字符输出流，并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        // 获取当前session对象，如果没有会话则返回null
        HttpSession session = req.getSession(false);

        if (session != null) {
            // 仅移除教师用户，而不影响学生
            session.removeAttribute("teacher"); // 仅移除教师
        }

        writer.write("<html><body>");
        writer.write("<h1>教师登出成功！</h1>");
        writer.write("<script>window.alert('教师登出成功');</script>");
        writer.write("<script>window.location.href='login.html';</script>");
        writer.write("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
