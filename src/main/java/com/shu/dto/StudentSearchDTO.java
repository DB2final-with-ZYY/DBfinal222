package com.shu.dto;

import java.math.BigDecimal;

public class StudentSearchDTO {
    private Integer studentId;
    private String studentName;
    private String gender;
    private Integer gradeNumber;
    private String departmentName;
    private String majorName;
    private String email;
    private String nativePlace;  // 生源地
    private BigDecimal grade; // 成绩分数
    private Integer usualScore;  // 平时成绩
    private Integer examScore;  // 考试成绩
    private String status;  // 状态
    private String password;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getGradeNumber() {
        return gradeNumber;
    }

    public void setGradeNumber(Integer gradeNumber) {
        this.gradeNumber = gradeNumber;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
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

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "StudentSearchDTO{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", gender='" + gender + '\'' +
                ", gradeNumber=" + gradeNumber +
                ", departmentName='" + departmentName + '\'' +
                ", majorName='" + majorName + '\'' +
                ", email='" + email + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", grade=" + grade +
                ", usualScore=" + usualScore +
                ", examScore=" + examScore +
                ", status='" + status + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}