package com.example.javaproject21;

/**
 * The class for Teacher course.
 */
public class TeacherCourse {
    /**
     * The String Course name.
     */
private String courseName;
    /**
     * The String Course teacher.
     */
private String courseTeacher;
    /**
     * The String Course code.
     */
private String courseCode;
    /**
     * The String Course credit.
     */
private String courseCredit;
    /**
     * The String Teacher designation.
     */
private String teacherDesignation;
    /**
     * The long variable for Priority.
     */
private
    long priority;

    /**
     * Gets teacher designation.
     *
     * @return the teacher designation
     */
    public String getTeacherDesignation() {
        return teacherDesignation;
    }

    /**
     * Sets teacher designation.
     *
     * @param teacherDesignation the teacher designation
     */
    public void setTeacherDesignation(String teacherDesignation) {
        this.teacherDesignation = teacherDesignation;
    }

    /**
     * Instantiates a new Teacher course.
     *
     * @param courseName         the course name
     * @param courseTeacher      the course teacher
     * @param courseCode         the course code
     * @param courseCredit       the course credit
     * @param teacherDesignation the teacher designation
     * @param priority           the priority
     */
    public TeacherCourse(String courseName, String courseTeacher, String courseCode, String courseCredit, String teacherDesignation, long priority) {
        this.courseName = courseName;
        this.courseTeacher = courseTeacher;
        this.courseCode = courseCode;
        this.courseCredit = courseCredit;
        this.teacherDesignation = teacherDesignation;
        this.priority = priority;
    }


    /**
     * Instantiates a new Teacher course.
     */
    public TeacherCourse() {
    }

    /**
     * Gets course name.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets course name.
     *
     * @param courseName the course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets course teacher.
     *
     * @return the course teacher
     */
    public String getCourseTeacher() {
        return courseTeacher;
    }

    /**
     * Sets course teacher.
     *
     * @param courseTeacher the course teacher
     */
    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    /**
     * Gets course code.
     *
     * @return the course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Sets course code.
     *
     * @param courseCode the course code
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Gets course credit.
     *
     * @return the course credit
     */
    public String getCourseCredit() {
        return courseCredit;
    }

    /**
     * Sets course credit.
     *
     * @param courseCredit the course credit
     */
    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    /**
     * Gets priority.
     *
     * @return the priority
     */
    public long getPriority() {
        return priority;
    }

    /**
     * Sets priority.
     *
     * @param priority the priority
     */
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
