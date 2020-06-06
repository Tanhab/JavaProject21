package com.example.javaproject21;

/**
 * The class for Notification.
 */
public class Notification {
    /**
     * The String for Message.
     */
private String message;
    /**
     * The String for Date.
     */
private String date;
    /**
     * The String for Sender.
     */
private String sender;
    /**
     * The String for Image uri.
     */
private String imageUri;
    /**
     * The String for Activity.
     */
private String activity;

    /**
     * Gets activity.
     *
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Sets activity.
     *
     * @param activity the activity
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * Instantiates a new Notification.
     *
     * @param message  the message
     * @param date     the date
     * @param sender   the sender
     * @param imageUri the image uri
     * @param activity the activity
     * @param priority the priority
     */
    public Notification(String message, String date, String sender, String imageUri, String activity, long priority) {
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.imageUri = imageUri;
        this.activity = activity;
        this.priority = priority;
    }

    /**
     * The long variable for Priority.
     */
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
     * Instantiates a new Notification.
     *
     * @param message  the message
     * @param date     the date
     * @param sender   the sender
     * @param imageUri the image uri
     * @param priority the priority
     */
    public Notification(String message, String date, String sender, String imageUri, long priority) {
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.imageUri = imageUri;
        this.priority = priority;
    }

    /**
     * Instantiates a new Notification.
     */
    public Notification() {
    }
}
