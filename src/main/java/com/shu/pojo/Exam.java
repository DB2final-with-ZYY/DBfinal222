package com.shu.pojo;

public class Exam {
    private Integer examId;
    private Integer scheduleId;
    private String examTime;
    private String examPlace;

    private ClassSchedule classSchedule;

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

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(ClassSchedule classSchedule) {
        this.classSchedule = classSchedule;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examId=" + examId +
                ", scheduleId=" + scheduleId +
                ", examTime='" + examTime + '\'' +
                ", examPlace='" + examPlace + '\'' +
                ", classSchedule=" + classSchedule +
                '}';
    }
}
