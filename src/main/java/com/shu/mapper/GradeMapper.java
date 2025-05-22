package com.shu.mapper;

import com.shu.pojo.Grade;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeMapper {
    /**
     * 更新成绩 需要enrollment_id grade
     * @param grade
     * @return
     */
    int update(Grade grade);

    /**
     * 插入成绩 需要enrollment_id grade
     * @param grade
     * @return
     */
    int insert(Grade grade);

    /**
     *  插入平时成绩 usual_score
     * @param grade
     * @return
     */
    int insertUsualScore(Grade grade);

    /**
     *  插入考试成绩 exam_score
     * @param grade
     * @return
     */
    int insertExamScore(Grade grade);

    /**
     *  更新平时成绩 usual_score
     * @param grade
     * @return
     */
    int updateUsualScore(Grade grade);

    /**
     *  更新考试成绩 exam_score
     * @param grade
     * @return
     */
    int updateExamScore(Grade grade);

    /**
     * 根据选课ID删除成绩
     * @param enrollmentId 选课ID
     * @return 影响的行数
     */
    int deleteByEnrollmentId(Integer enrollmentId);
} 