package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.ExamMapper;
import com.shu.pojo.Exam;
import com.shu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/addExam")
public class AddExamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // 读取请求体中的JSON数据
            Exam exam = objectMapper.readValue(request.getReader(), Exam.class);

            // 校验必填字段
            if (exam.getScheduleId() == null || exam.getExamTime() == null || exam.getExamPlace() == null
                    || exam.getExamTime().trim().isEmpty() || exam.getExamPlace().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "必填字段不能为空");
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }

            // 获取SqlSession
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                ExamMapper examMapper = sqlSession.getMapper(ExamMapper.class);

                // 插入考试安排
                int insertResult = examMapper.insert(exam);

                // 提交事务
                sqlSession.commit();

                if (insertResult > 0) {
                    result.put("success", true);
                    result.put("message", "新增考试安排成功");
                } else {
                    result.put("success", false);
                    result.put("message", "新增考试安排失败");
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增考试安排时发生错误: " + e.getMessage());
        }

        objectMapper.writeValue(response.getWriter(), result);
    }
}