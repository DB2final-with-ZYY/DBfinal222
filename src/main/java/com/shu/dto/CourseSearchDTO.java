package com.shu.dto;

import java.math.BigDecimal;

public class CourseSearchDTO {
    private Integer scheduleId;
    private Integer courseId;          // 课程号
    private String courseName;         // 课程名
    private Integer credit;            // 学分
    private Integer teacherId;         // 教师号
    private String teacherName;        // 教师姓名
    private String position;           // 教师职称
    private String classTime;          // 上课时间
    private String classroom;          // 教室
    private Integer capacity;          // 课容量
    private Integer enrolledCount;     // 已选人数
    private String examTime;           // 考试时间
    private String examPlace;          // 考试地点
    private String examWeight;         // 考试占比
    private Integer usualScore;        // 平时成绩
    private Integer examScore;         // 考试成绩
    private BigDecimal grade;          // 成绩
    private Integer departmentId;
    private String departmentName;
    private Integer semester;
    private String status;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(Integer enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public String getExamPlace() {
        return examPlace;
    }

    public void setExamPlace(String examPlace) {
        this.examPlace = examPlace;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getExamWeight() {
        return examWeight;
    }

    public void setExamWeight(String examWeight) {
        this.examWeight = examWeight;
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CourseSearchDTO{" +
                "scheduleId=" + scheduleId +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", credit=" + credit +
                ", teacherId=" + teacherId +
                ", teacherName='" + teacherName + '\'' +
                ", position='" + position + '\'' +
                ", classTime='" + classTime + '\'' +
                ", classroom='" + classroom + '\'' +
                ", capacity=" + capacity +
                ", enrolledCount=" + enrolledCount +
                ", examTime='" + examTime + '\'' +
                ", examPlace='" + examPlace + '\'' +
                ", examWeight='" + examWeight + '\'' +
                ", usualScore=" + usualScore +
                ", examScore=" + examScore +
                ", grade=" + grade +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", semester=" + semester +
                ", status='" + status + '\'' +
                '}';
    }
}