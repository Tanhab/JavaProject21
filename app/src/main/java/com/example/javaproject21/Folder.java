package com.example.javaproject21;

/**
 * The Folder class.
 */
public class Folder {
    /**
     * The String variable for folder name.
     */
    String folderName;
    /**
     * The String variable for creator of the folder.
     */
    String creator;
    /**
     * The String variable for date.
     */
    String date;
    /**
     * The String variable for path of the folder
     */
    String path;
    /**
     * The long variable for Priority.
     */
    long priority;

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
     * Instantiates a new Folder.
     *
     * @param folderName the folder name
     * @param creator    the creator
     * @param date       the date
     * @param path       the path
     * @param priority   the priority
     */
    public Folder(String folderName, String creator, String date, String path, long priority) {
        this.folderName = folderName;
        this.creator = creator;
        this.date = date;
        this.path = path;
        this.priority = priority;
    }

    /**
     * Instantiates a new Folder.
     */
    public Folder() {
    }

    /**
     * Instantiates a new Folder.
     *
     * @param folderName the folder name
     * @param creator    the creator
     * @param date       the date
     * @param path       the path
     */
    public Folder(String folderName, String creator, String date, String path) {
        this.folderName = folderName;
        this.creator = creator;
        this.date = date;
        this.path = path;
    }

    /**
     * Gets folder name.
     *
     * @return the folder name
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Sets folder name.
     *
     * @param folderName the folder name
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * Gets creator.
     *
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets creator.
     *
     * @param creator the creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
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
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "folderName='" + folderName + '\'' +
                ", creator='" + creator + '\'' +
                ", date='" + date + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
