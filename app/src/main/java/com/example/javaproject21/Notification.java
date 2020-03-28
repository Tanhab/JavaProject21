package com.example.javaproject21;

public class Notification {
    String title,body,date,time,sender;
    long priority;

    public Notification(String title, String body, String date, long a) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.priority = a;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public Notification(String title, String body, String date, String sender, long priority) {
        this.title = title;
        this.body = body;
        this.date = date;

        this.sender = sender;
        this.priority = priority;
    }

    public Notification() {
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
