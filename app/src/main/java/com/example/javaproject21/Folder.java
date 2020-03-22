package com.example.javaproject21;

public class Folder {
    String folderName,creator,date,path;
    long priority;

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public Folder(String folderName, String creator, String date, String path, long priority) {
        this.folderName = folderName;
        this.creator = creator;
        this.date = date;
        this.path = path;
        this.priority = priority;
    }

    public Folder() {
    }

    public Folder(String folderName, String creator, String date, String path) {
        this.folderName = folderName;
        this.creator = creator;
        this.date = date;
        this.path = path;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

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
