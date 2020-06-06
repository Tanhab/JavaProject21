package com.example.javaproject21;

/**
 * The class for Exam routine.
 */
public class ExamRoutine {
    /**
     * The String for Exams.
     */
private String exams;
    /**
     * The String for Exam date.
     */
private String examDate;
    /**
     * The long variable for Priority.
     */
private long priority;
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
     * The String for Post id.
     */
private String postID;

    @Override
    public String toString() {
        return "ExamRoutine{" +
                "exams='" + exams + '\'' +
                ", examDate='" + examDate + '\'' +
                ", priority=" + priority +
                ", sender='" + sender + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", postID='" + postID + '\'' +
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
     * Instantiates a new Exam routine.
     *
     * @param exams    the exams
     * @param examDate the exam date
     * @param priority the priority
     * @param sender   the sender
     * @param imageUri the image uri
     * @param message  the message
     * @param date     the date
     * @param postID   the post id
     */
    public ExamRoutine(String exams, String examDate, long priority, String sender, String imageUri, String message, String date, String postID) {
        this.exams = exams;
        this.examDate = examDate;
        this.priority = priority;
        this.sender = sender;
        this.imageUri = imageUri;
        this.message = message;
        this.date = date;
        this.postID = postID;
    }


    /**
     * Instantiates a new Exam routine.
     */
    public ExamRoutine() {
    }

    /**
     * Gets exams.
     *
     * @return the exams
     */
    public String getExams() {
        return exams;
    }

    /**
     * Sets exams.
     *
     * @param exams the exams
     */
    public void setExams(String exams) {
        this.exams = exams;
    }

    /**
     * Gets exam date.
     *
     * @return the exam date
     */
    public String getExamDate() {
        return examDate;
    }

    /**
     * Sets exam date.
     *
     * @param examDate the exam date
     */
    public void setExamDate(String examDate) {
        this.examDate = examDate;
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