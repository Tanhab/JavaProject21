package com.example.javaproject21;

public class ExamRoutine {
    private String examName,examDate,examTime,syllabus,resources;
    private long priority;

    public ExamRoutine(String examName, String examDate, String examTime, String syllabus, String resources, long priority) {
        this.examName = examName;
        this.examDate = examDate;
        this.examTime = examTime;
        this.syllabus = syllabus;
        this.resources = resources;
        this.priority = priority;
    }

    public ExamRoutine() {
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "ExamRoutine{" +
                "examName='" + examName + '\'' +
                ", examDate='" + examDate + '\'' +
                ", examTime='" + examTime + '\'' +
                ", syllabus='" + syllabus + '\'' +
                ", resources='" + resources + '\'' +
                ", priority=" + priority +
                '}';
    }
}
