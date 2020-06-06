package com.example.javaproject21;

/**
 * The class for Poll.
 */
public class Poll {
    /**
     * The String for Sender.
     */
private String sender;
    /**
     * The String for Message.
     */
private String message;
    /**
     * The String for Date.
     */
private String date;
    /**
     * The String for Poll id.
     */
private String pollId;
    /**
     * The String forImage uri.
     */
private String imageUri;
    /**
     * The Priority.
     */
    long priority;

    /**
     * Instantiates a new Poll.
     */
    public Poll() {
    }

    /**
     * Instantiates a new Poll.
     *
     * @param sender   the sender
     * @param message  the message
     * @param date     the date
     * @param pollId   the poll id
     * @param imageUri the image uri
     * @param priority the priority
     */
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
     * Gets poll id.
     *
     * @return the poll id
     */
    public String getPollId() {
        return pollId;
    }

    /**
     * Sets poll id.
     *
     * @param pollId the poll id
     */
    public void setPollId(String pollId) {
        this.pollId = pollId;
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
