package com.example.ntumobile;


public class social_jio_sports_session {

    private String Activity;
    private String Location;
    private String Time;
    private String Pax;
    private String CurrPax;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public social_jio_sports_session() {}

    // Getter and setter method

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String Activity) {
        this.Activity = Activity;
    }

    public String getLocation() {

        return Location;
    }

    public void setLocation(String Location) {

        this.Location = Location;
    }

    public String getTime() {

        return Time;
    }

    public void setTime(String Time) {

        this.Time = Time;
    }

    public String getPax() {
        return Pax;
    }

    public void setPax(String Pax) {
        this.Pax = Pax;
    }

    public String getCurrPax() {
        return CurrPax;
    }

    public void setCurrPax(String CurrPax) {
        this.CurrPax = CurrPax;
    }
}
