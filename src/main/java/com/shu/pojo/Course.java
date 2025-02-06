package com.shu.pojo;

public class Course {
    private Integer courseId; // 课程ID
    private String courseName; // 课程名称
    private Integer departmentId; // 所属院系ID
    private Integer credit; // 课程学分

    // 关联属性
    private Department department; // 所属院系信息

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", departmentId=" + departmentId +
                ", credit=" + credit +
                ", department=" + department +
                '}';
    }
}