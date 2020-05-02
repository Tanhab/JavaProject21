package com.example.javaproject21;

public class Event {
    private String eventName,eventPlace,sender,imageUri,message,time,eventDate,date,postID;
    long priority;

    public String getEventName() {
        return eventName;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Event(String eventName, String eventPlace, String sender, String imageUri, String message, String time, String eventDate, String date, String postID, long priority) {
        this.eventName = eventName;
        this.eventPlace = eventPlace;
        this.sender = sender;
        this.imageUri = imageUri;
        this.message = message;
        this.time = time;
        this.eventDate = eventDate;
        this.date = date;
        this.postID = postID;
        this.priority = priority;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "sender='" + sender + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", postID='" + postID + '\'' +
                ", priority=" + priority +
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }
}
