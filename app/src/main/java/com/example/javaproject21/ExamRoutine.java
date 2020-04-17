package com.example.javaproject21;

public class ExamRoutine {
    private String exams, examDate;
    private long priority;

    public ExamRoutine(String exams, String examDate, long priority) {
        this.exams = exams;
        this.examDate = examDate;

        this.priority = priority;
    }

    public ExamRoutine() {
    }

    public String getExams() {
        return exams;
    }

    public void setExams(String exams) {
        this.exams = exams;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
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
                "exams='" + exams + '\'' +
                ", examDate='" + examDate + '\'' +
                ", priority=" + priority +
                '}';
    }
}