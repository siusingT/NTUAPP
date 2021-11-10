package com.example.ntumobile;

public class Friends {

    String email, name, course, userID, image,onlineStatus,typingTo, search;

    public Friends(){
    }

    public Friends(String email, String name, String course, String image, String userID, String onlineStatus, String typingTo, String search){
        this.name = name;
        this.course = course;
        this.email = email;
        this.userID = userID;
        this.image = image;
        this.onlineStatus = onlineStatus;
        this.typingTo = typingTo;
        this.search = search;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {

        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
