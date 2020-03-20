package com.example.javaproject21;


import com.google.firebase.Timestamp;

public class ClassRoutine  {
    private static final String TAG = "ClassRoutine";

    private String date,section,classes;
    private long priority;


    public ClassRoutine(String date, String section, String classes, long priority) {
        this.date = date;
        this.section = section;
        this.classes = classes;
        this.priority = priority;
    }

    public ClassRoutine(String date, String section, String classes) {
        this.date = date;
        this.section = section;
        this.classes = classes;
    }

    public ClassRoutine() {
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "ClassRoutine{" +
                "date='" + date + '\'' +
                ", section='" + section + '\'' +
                ", classes=" + classes +
                '}';
    }
}
