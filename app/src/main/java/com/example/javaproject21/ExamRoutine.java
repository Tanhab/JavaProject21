package com.example.javaproject21;

public class ExamRoutine {
    private String exams, examDate;
    private long priority;
    private String sender,imageUri,message,date,postID;

    @Override
    public String toString() {
        return "ExamRoutine{" +
                "exams='" + exams + '\'' +
                ", examDate='" + examDate + '\'' +
                ", priority=" + priority +
                ", sender='" + sender + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", postID='" + postID + '\'' +
                '}';
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public ExamRoutine(String exams, String examDate, long priority, String sender, String imageUri, String message, String date, String postID) {
        this.exams = exams;
        this.examDate = examDate;
        this.priority = priority;
        this.sender = sender;
        this.imageUri = imageUri;
        this.message = message;
        this.date = date;
        this.postID = postID;
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

}