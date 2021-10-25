package com.example.ntumobile;

public class user {

    String email, name, course;

    public user(){

    }


    public user(String email, String name, String course){
        this.name = name;
        this.course= course;
        this.email=email;
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
}
