package com.shu.pojo;

public class Enrollment {
    private Integer enrollmentId; // 选课ID
    private Integer studentId; // 学生ID
    private Integer scheduleId; // 课程安排ID

    // 关联属性
    private Student student; // 学生信息
    private ClassSchedule classSchedule; // 课程安排信息

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(ClassSchedule classSchedule) {
        this.classSchedule = classSchedule;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", studentId=" + studentId +
                ", scheduleId=" + scheduleId +
                ", student=" + student +
                ", classSchedule=" + classSchedule +
                '}';
    }
}