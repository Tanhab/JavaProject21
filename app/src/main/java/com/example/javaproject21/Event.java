package com.example.javaproject21;

/**
 * The class for Event.
 */
public class Event {
    /**
     * The String for Event name.
     */
private String eventName;
    /**
     * The String for Event place.
     */
private String eventPlace;
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
     * The String for Time.
     */
private String time;
    /**
     * The String for Event date.
     */
private String eventDate;
    /**
     * The String for Date.
     */
private String date;
    /**
     * The String for Post id.
     */
private String postID;
    /**
     * The long variable for Priority.
     */
    long priority;

    /**
     * Gets event name.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Gets event place.
     *
     * @return the event place
     */
    public String getEventPlace() {
        return eventPlace;
    }

    /**
     * Sets event place.
     *
     * @param eventPlace the event place
     */
    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    /**
     * Sets event name.
     *
     * @param eventName the event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Instantiates a new Event.
     *
     * @param eventName  the event name
     * @param eventPlace the event place
     * @param sender     the sender
     * @param imageUri   the image uri
     * @param message    the message
     * @param time       the time
     * @param eventDate  the event date
     * @param date       the date
     * @param postID     the post id
     * @param priority   the priority
     */
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

    /**
     * Gets event date.
     *
     * @return the event date
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Sets event date.
     *
     * @param eventDate the event date
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Instantiates a new Event.
     */
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
     * Gets time.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(String time) {
        this.time = time;
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
