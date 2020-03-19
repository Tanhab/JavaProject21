package com.example.javaproject21;


public class ClassRoutine  {
    private static final String TAG = "ClassRoutine";

    private String date,section,classes;


    public ClassRoutine(String date, String section, String classes) {
        this.date = date;
        this.section = section;
        this.classes = classes;
    }

    public ClassRoutine() {
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
