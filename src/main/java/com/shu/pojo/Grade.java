package com.shu.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Grade {
    private Integer gradeId; // 成绩ID
    private Integer enrollmentId; // 选课ID
    private BigDecimal grade; // 成绩分数
    private Date examDate; // 考试日期

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

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", enrollmentId=" + enrollmentId +
                ", grade=" + grade +
                ", examDate=" + examDate +
                ", enrollment=" + enrollment +
                '}';
    }
}