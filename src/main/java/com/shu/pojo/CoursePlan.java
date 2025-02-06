package com.shu.pojo;

public class CoursePlan {
    private Integer planId; // 选课方案ID
    private Integer majorId; // 专业ID
    private Integer courseId; // 课程ID

    // 关联属性
    private Major major; // 专业信息
    private Course course; // 课程信息

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "CoursePlan{" +
                "planId=" + planId +
                ", majorId=" + majorId +
                ", courseId=" + courseId +
                ", major=" + major +
                ", course=" + course +
                '}';
    }
}