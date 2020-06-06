package com.example.javaproject21;

/**
 * The class for Comment.
 */
public class Comment {
    /**
     * The String variable for Sender.
     */
private String sender;
    /**
     * The String variable for Message.
     */
private String message;
    /**
     * The String variable for Date.
     */
private String date;
    /**
     * The String variable for Image uri.
     */
private String imageUri;
    /**
     * The long variable for Priority.
     */
private long priority;

    /**
     * Instantiates a new Comment.
     */
    public Comment() {
    }

    /**
     * Instantiates a new Comment.
     *
     * @param sender   the sender
     * @param message  the message
     * @param date     the date
     * @param imageUri the image uri
     * @param priority the priority
     */
    public Comment(String sender, String message, String date, String imageUri, long priority) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.imageUri = imageUri;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", priority=" + priority +
                '}';
    }

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
}
