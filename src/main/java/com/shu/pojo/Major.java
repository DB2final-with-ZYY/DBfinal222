package com.shu.pojo;

public class Major {
    private Integer majorId; // 专业ID
    private String majorName; // 专业名称
    private Integer departmentId; // 所属院系ID

    // 关联属性
    private Department department; // 所属院系信息

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
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
        return "Major{" +
                "majorId=" + majorId +
                ", majorName='" + majorName + '\'' +
                ", departmentId=" + departmentId +
                ", department=" + department +
                '}';
    }
}