package com.shu.mapper;

import com.shu.pojo.Exam;
import javax.servlet.annotation.WebServlet;

@WebServlet
public interface ExamMapper {
    /**
     * 插入考试 需要schedule_id exam_time exam_place
     * @param exam
     * @return
     */
    int insert(Exam exam);
}
