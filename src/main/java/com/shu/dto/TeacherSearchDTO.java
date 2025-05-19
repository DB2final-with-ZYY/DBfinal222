package com.shu.dto;

public class TeacherSearchDTO {
    private Integer teacherId;
    private String password;
    private String teacherName;
    private String email;
    private String position;
    private String gender;
    private Integer departmentId;
    private String status;
    private String departmentName;
    private String courseName;  // 通过class_schedule关联的课程名称
    private Integer scheduleId; // 课程安排ID

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "TeacherSearchDTO{" +
                "teacherId=" + teacherId +
                ", password='" + password + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", gender='" + gender + '\'' +
                ", departmentId=" + departmentId +
                ", status='" + status + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", scheduleId=" + scheduleId +
                '}';
    }
}
