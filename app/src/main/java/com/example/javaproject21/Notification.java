package com.example.javaproject21;

public class Notification {
    private String message,date,sender,imageUri,activity;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Notification(String message, String date, String sender, String imageUri, String activity, long priority) {
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.imageUri = imageUri;
        this.activity = activity;
        this.priority = priority;
    }

    private long priority;

    @Override
    public String toString() {
        return "Notification{" +
                "message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", sender='" + sender + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", priority=" + priority +
                '}';
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

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public Notification(String message, String date, String sender, String imageUri, long priority) {
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.imageUri = imageUri;
        this.priority = priority;
    }

    public Notification() {
    }
}
