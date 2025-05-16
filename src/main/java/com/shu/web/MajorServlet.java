package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.MajorMapper;
import com.shu.pojo.Major;
import com.shu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/majors")
public class MajorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String departmentId = request.getParameter("departmentId");
        System.out.println("Received request for department ID: " + departmentId);

        // 获取SqlSession
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取MajorMapper
            MajorMapper majorMapper = sqlSession.getMapper(MajorMapper.class);

            // 查询专业列表
            List<Major> majors = majorMapper.selectMajorsByDepartmentId(Integer.parseInt(departmentId));
            System.out.println("Found " + majors.size() + " majors for department ID: " + departmentId);
            
            // 转换为JSON并响应
            objectMapper.writeValue(response.getWriter(), majors);
        } catch (Exception e) {
            System.err.println("Error querying majors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
} 