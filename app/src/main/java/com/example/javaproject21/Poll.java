package com.example.javaproject21;

public class Poll {
    private String sender,message,date,pollId,imageUri;
    long priority;

    public Poll() {
    }

    public Poll(String sender, String message, String date, String pollId, String imageUri, long priority) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.pollId = pollId;
        this.imageUri = imageUri;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", pollId='" + pollId + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", priority=" + priority +
                '}';
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
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
}
