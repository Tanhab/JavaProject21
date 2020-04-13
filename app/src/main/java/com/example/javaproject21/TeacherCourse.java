package com.example.javaproject21;

public class TeacherCourse {
    private String courseName,courseTeacher,courseCode,courseCredit;
    private long priority;


    public TeacherCourse(String courseName, String courseTeacher, String courseCode, String courseCredit,long priority) {
        this.courseName = courseName;
        this.courseTeacher = courseTeacher;
        this.courseCode = courseCode;
        this.courseCredit = courseCredit;
        this.priority = priority;
    }

    public TeacherCourse() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TeacherCourse{" +
                "courseName='" + courseName + '\'' +
                ", courseTeacher='" + courseTeacher + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", courseCredit='" + courseCredit + '\'' +
                ", priority=" + priority +
                '}';
    }
}
