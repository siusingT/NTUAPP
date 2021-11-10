package com.example.ntumobile.models;

public class ModelUser {
    public String name, course;
    String email;
    String search;
    public String image;
    String uid, userID;
    public String onlineStatus;
    String typingTo;

    public ModelUser() {
    }

    public ModelUser(String name, String course, String email, String search, String image, String uid, String userID, String onlineStatus, String typingTo) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.search = search;
        this.image = image;
        this.uid = uid;
        this.userID = userID;
        this.onlineStatus = onlineStatus;
        this.typingTo = typingTo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getTypingTo() {
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }
}
