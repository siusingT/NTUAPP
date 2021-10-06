package com.example.addfriend;

public class Users {

    public String username, fullname, school;

    public Users() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Users(String username, String fullname, String school) {
        this.username = username;
        this.fullname = fullname;
        this.school = school;
    }
}
