package com.shu.pojo;

public class ClassSchedule {
    private Integer scheduleId; // 排课ID
    private Integer semester; // 学期 (格式：202501)
    private Integer courseId; // 课程ID
    private Integer teacherId; // 教师ID
    private String classroom; // 教室
    private Integer capacity; // 课程容量
    private String classTime; // 上课时间 (格式：星期几几到几)

    // 关联属性
    private Course course; // 课程信息
    private Teacher teacher; // 教师信息

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "ClassSchedule{" +
                "scheduleId=" + scheduleId +
                ", semester=" + semester +
                ", courseId=" + courseId +
                ", teacherId=" + teacherId +
                ", classroom='" + classroom + '\'' +
                ", capacity=" + capacity +
                ", classTime='" + classTime + '\'' +
                ", course=" + course +
                ", teacher=" + teacher +
                '}';
    }
}