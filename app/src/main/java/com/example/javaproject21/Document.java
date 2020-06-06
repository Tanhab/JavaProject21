package com.example.javaproject21;

/**
 * The Document class.
 */
public class Document {
    /**
     * The String variable for name.
     */
    String name;
    /**
     * The String variable for date.
     */
    String date;
    /**
     * The String variable for url.
     */
    String url;
    /**
     * The String variable for Uploader.
     */
    String uploader;
    /**
     * The String variable for folder.
     */
    String folder;
    /**
     * The long variable for Priority.
     */
    long priority;

    /**
     * Instantiates a new Document.
     */
    public Document() {
    }

    /**
     * Instantiates a new Document.
     *
     * @param name     the name
     * @param date     the date
     * @param url      the url
     * @param uploader the uploader
     * @param folder   the folder
     * @param priority the priority
     */
    public Document(String name, String date, String url, String uploader, String folder, long priority) {
        this.name = name;
        this.date = date;
        this.url = url;
        this.uploader = uploader;
        this.folder = folder;
        this.priority = priority;
    }

    /**
     * Gets folder.
     *
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * Sets folder.
     *
     * @param folder the folder
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * Instantiates a new Document.
     *
     * @param name     the name
     * @param date     the date
     * @param url      the url
     * @param uploader the uploader
     * @param priority the priority
     */
    public Document(String name, String date, String url, String uploader, long priority) {
        this.name = name;
        this.date = date;
        this.url = url;
        this.uploader = uploader;
        this.priority = priority;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
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
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets uploader.
     *
     * @return the uploader
     */
    public String getUploader() {
        return uploader;
    }

    /**
     * Sets uploader.
     *
     * @param uploader the uploader
     */
    public void setUploader(String uploader) {
        this.uploader = uploader;
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

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", uploader='" + uploader + '\'' +
                ", priority=" + priority +
                '}';
    }
}
