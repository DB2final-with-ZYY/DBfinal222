package com.shu.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Grade {
    private Integer gradeId; // 成绩ID
    private Integer enrollmentId; // 选课ID
    private Integer usualScore;  // 平时成绩
    private Integer examScore;  // 考试成绩
    private BigDecimal grade; // 成绩分数

    // 关联属性
    private Enrollment enrollment; // 选课信息

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Integer getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(Integer usualScore) {
        this.usualScore = usualScore;
    }

    public Integer getExamScore() {
        return examScore;
    }

    public void setExamScore(Integer examScore) {
        this.examScore = examScore;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", enrollmentId=" + enrollmentId +
                ", usualScore=" + usualScore +
                ", examScore=" + examScore +
                ", grade=" + grade +
                ", enrollment=" + enrollment +
                '}';
    }
}