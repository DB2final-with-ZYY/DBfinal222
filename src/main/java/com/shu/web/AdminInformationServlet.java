package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.DepartmentMapper;
import com.shu.mapper.AdminMapper;
import com.shu.pojo.Department;
import com.shu.pojo.Admin;
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

@WebServlet("/adminInfo")
public class AdminInformationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码和内容类型
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前登录的管理员ID
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("admin");

        if (admin == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"未登录\"}");
            return;
        }

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            // 获取AdminMapper
            AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);

            // 查询管理员信息
            admin = adminMapper.selectById(admin.getAdminId());

            if (admin == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"管理员信息不存在\"}");
                return;
            }

            // 获取学院mapper
            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
            // 查学院名称
            Department department = departmentMapper.selectById(admin.getDepartmentId());

            // 创建返回的数据对象
            Map<String, Object> data = new HashMap<>();
            data.put("adminId", admin.getAdminId());
            data.put("name", admin.getName());
            data.put("gender", admin.getGender().equals("M") ? "男" : admin.getGender().equals("F") ? "女" : "其他");
            data.put("email", admin.getEmail());
            data.put("departmentName", department.getDepartmentName());

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