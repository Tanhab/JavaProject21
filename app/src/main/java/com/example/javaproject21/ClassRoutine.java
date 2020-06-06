package com.example.javaproject21;


import com.google.firebase.Timestamp;

/**
 * The class for class routine.
 */
public class ClassRoutine  {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "ClassRoutine";

    /**
     * The string variable for Routine date.
     */
private String routineDate;
    /**
     * The string variable for Section.
     */
private String section;
    /**
     * The string variable for Classes.
     */
private String classes;

    /**
     * The string variable for Sender.
     */
private String sender;
    /**
     * The string variable for Image uri.
     */
private String imageUri;
    /**
     * The string variable for Message.
     */
private String message;
    /**
     * The string variable for Date.
     */
private String date;
    /**
     * The string variable for Post id.
     */
private String postID;

    /**
     * The long variable for Priority.
     */
private long priority;

    /**
     * Gets routine date.
     *
     * @return the routine date
     */
    public String getRoutineDate() {
        return routineDate;
    }

    /**
     * Sets routine date.
     *
     * @param routineDate the routine date
     */
    public void setRoutineDate(String routineDate) {
        this.routineDate = routineDate;
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
     * Instantiates a new Class routine.
     *
     * @param routineDate the routine date
     * @param section     the section
     * @param classes     the classes
     * @param sender      the sender
     * @param imageUri    the image uri
     * @param message     the message
     * @param date        the date
     * @param postID      the post id
     * @param priority    the priority
     */
    public ClassRoutine(String routineDate, String section, String classes, String sender, String imageUri, String message, String date, String postID, long priority) {
        this.routineDate = routineDate;
        this.section = section;
        this.classes = classes;
        this.sender = sender;
        this.imageUri = imageUri;
        this.message = message;
        this.date = date;
        this.postID = postID;
        this.priority = priority;
    }


    /**
     * Instantiates a new Class routine.
     *
     * @param date     the date
     * @param section  the section
     * @param classes  the classes
     * @param priority the priority
     */
    public ClassRoutine(String date, String section, String classes, long priority) {
        this.date = date;
        this.section = section;
        this.classes = classes;
        this.priority = priority;
    }

    /**
     * Instantiates a new Class routine.
     *
     * @param date    the date
     * @param section the section
     * @param classes the classes
     */
    public ClassRoutine(String date, String section, String classes) {
        this.date = date;
        this.section = section;
        this.classes = classes;
    }

    /**
     * Instantiates a new Class routine.
     */
    public ClassRoutine() {
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

    /**
     * Gets section.
     *
     * @return the section
     */
    public String getSection() {
        return section;
    }

    /**
     * Sets section.
     *
     * @param section the section
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * Gets classes.
     *
     * @return the classes
     */
    public String getClasses() {
        return classes;
    }

    /**
     * Sets classes.
     *
     * @param classes the classes
     */
    public void setClasses(String classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "ClassRoutine{" +
                "date='" + date + '\'' +
                ", section='" + section + '\'' +
                ", classes=" + classes +
                '}';
    }
}
