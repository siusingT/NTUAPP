package com.example.ntumobile;

public class booking_display {

    private String TR;
    private String Date;
    private String Time;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public booking_display() {
    }

    // Getter and setter method

    public String getTR() {
        return TR;
    }

    public void setTR(String Activity) {
        this.TR = TR;
    }

    public String getDate() {

        return Date;
    }

    public void setDate(String Date) {

        this.Date = Date;
    }

    public String getTime() {

        return Time;
    }

    public void setTime(String Time) {

        this.Time = Time;
    }
}