package com.shu.pojo;

public class Student {
    private Integer studentId; // 学生ID
    private String password; // 登录密码
    private String email; // 电子邮箱
    private String name; // 学生姓名
    private String gender; // 性别 (M-男,F-女,O-其他)
    private String gradeNumber;   // 年级数
    private String nativePlace; // 生源地
    private Integer departmentId; // 所属院系ID
    private Integer majorId; // 所属专业ID
    private String status; // 学生状态 (正常,试读,休学,毕业)

    // 关联属性
    private Department department; // 所属院系信息
    private Major major; // 所属专业信息

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGradeNumber() {
        return gradeNumber;
    }

    public void setGradeNumber(String gradeNumber) {
        this.gradeNumber = gradeNumber;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", gradeNumber='" + gradeNumber + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", departmentId=" + departmentId +
                ", majorId=" + majorId +
                ", status='" + status + '\'' +
                ", department=" + department +
                ", major=" + major +
                '}';
    }
}