package com.shu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.mapper.ClassScheduleMapper;
import com.shu.pojo.ClassSchedule;
import com.shu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/updateSchedule")
public class UpdateClassScheduleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // 读取请求体
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // 解析JSON数据
            ClassSchedule schedule = objectMapper.readValue(json.toString(), ClassSchedule.class);

            // 获取SqlSession
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                // 获取ClassScheduleMapper
                ClassScheduleMapper scheduleMapper = sqlSession.getMapper(ClassScheduleMapper.class);

                // 验证课程和教师是否存在
                if (!validateSchedule(schedule, sqlSession)) {
                    result.put("success", false);
                    result.put("message", "更新失败：课程或教师不存在");
                    objectMapper.writeValue(response.getWriter(), result);
                    return;
                }

                // 更新课程安排信息
                int rowsAffected = scheduleMapper.updateClassSchedule(schedule);
                
                if (rowsAffected > 0) {
                    sqlSession.commit();
                    result.put("success", true);
                } else {
                    result.put("success", false);
                    result.put("message", "更新失败：未找到对应的课程安排记录");
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "服务器错误：" + e.getMessage());
        }

        // 发送响应
        objectMapper.writeValue(response.getWriter(), result);
    }

    private boolean validateSchedule(ClassSchedule schedule, SqlSession sqlSession) {
        try {
            // 这里可以添加验证逻辑，例如：
            // 1. 检查课程是否存在
            // 2. 检查教师是否存在
            // 3. 检查教室在该时间段是否可用
            // 4. 检查教师在该时间段是否有其他课程
            // 等等...
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
