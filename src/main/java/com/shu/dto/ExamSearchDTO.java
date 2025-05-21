package com.shu.dto;

public class ExamSearchDTO {
    private Integer examId;        // 考试ID
    private Integer scheduleId;    // 课程安排ID
    private Integer courseId;      // 课程号
    private String courseName;     // 课程名称
    private Integer teacherId;     // 教师号
    private String teacherName;    // 教师姓名（可选）
    private String examTime;       // 考试时间
    private String examPlace;      // 考试地点

    // getter & setter
    public Integer getExamId() {
        return examId;
    }
    public void setExamId(Integer examId) {
        this.examId = examId;
    }

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

    public String getExamTime() {
        return examTime;
    }
    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getExamPlace() {
        return examPlace;
    }
    public void setExamPlace(String examPlace) {
        this.examPlace = examPlace;
    }
}
