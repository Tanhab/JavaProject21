package com.example.javaproject21;


import com.google.firebase.Timestamp;

public class ClassRoutine  {
    private static final String TAG = "ClassRoutine";

    private String routineDate,section,classes;

    public String getRoutineDate() {
        return routineDate;
    }

    public void setRoutineDate(String routineDate) {
        this.routineDate = routineDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    private String sender,imageUri,message,date,postID;

    public ClassRoutine(String routineDate, String section, String classes, String sender, String imageUri, String message, String date, String postID, long priority) {
        this.routineDate = routineDate;
        this.section = section;
        this.classes = classes;
        this.sender = sender;
        this.imageUri = imageUri;
        this.message = message;
        this.date = date;
        this.postID = postID;
        this.priority = priority;
    }

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
