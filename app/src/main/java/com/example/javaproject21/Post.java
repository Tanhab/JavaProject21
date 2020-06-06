package com.example.javaproject21;

import java.util.List;

/**
 * The class for Post.
 */
public class Post {
    /**
     * The String for Sender.
     */
private String sender;
    /**
     * The String for Image uri.
     */
private String imageUri;
    /**
     * The String for Message.
     */
private String message;
    /**
     * The String for Date.
     */
private String date;
    /**
     * The Post id.
     */
private String postID;
    /**
     * The Priority.
     */
    long priority;

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets sender.
     *
     * @param sender the sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Gets image uri.
     *
     * @return the image uri
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * Sets image uri.
     *
     * @param imageUri the image uri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Instantiates a new Post.
     */
    public Post() {
    }

    /**
     * Gets post id.
     *
     * @return the post id
     */
    public String getPostID() {
        return postID;
    }

    /**
     * Sets post id.
     *
     * @param postID the post id
     */
    public void setPostID(String postID) {
        this.postID = postID;
    }

    /**
     * Instantiates a new Post.
     *
     * @param sender   the sender
     * @param imageUri the image uri
     * @param message  the message
     * @param date     the date
     * @param postID   the post id
     * @param priority the priority
     */
    public Post(String sender, String imageUri, String message, String date, String postID, long priority) {
        this.sender = sender;
        this.imageUri = imageUri;
        this.message = message;
        this.date = date;
        this.postID = postID;
        this.priority = priority;
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

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{" +
                "sender='" + sender + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", postID='" + postID + '\'' +
                ", priority=" + priority +
                '}';
    }
}
