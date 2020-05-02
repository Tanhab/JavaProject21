package com.example.javaproject21;

public class Document {
    String name,date,url,uploader,folder;
    long priority;

    public Document() {
    }

    public Document(String name, String date, String url, String uploader, String folder, long priority) {
        this.name = name;
        this.date = date;
        this.url = url;
        this.uploader = uploader;
        this.folder = folder;
        this.priority = priority;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Document(String name, String date, String url, String uploader, long priority) {
        this.name = name;
        this.date = date;
        this.url = url;
        this.uploader = uploader;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public long getPriority() {
        return priority;
    }

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
