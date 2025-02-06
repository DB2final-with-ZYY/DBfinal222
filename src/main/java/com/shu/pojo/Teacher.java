package com.shu.pojo;

public class Teacher {
    private Integer teacherId; // 教师ID
    private String name; // 教师姓名
    private String email;
    private String position; // 职位 (正教授,副教授,高级工程师,讲师)
    private String gender; // 性别 (M-男,F-女,O-其他)
    private Integer departmentId; // 所属院系ID

    // 关联属性
    private Department department; // 所属院系信息

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", gender='" + gender + '\'' +
                ", departmentId=" + departmentId +
                ", department=" + department +
                '}';
    }
}