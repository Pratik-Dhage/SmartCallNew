package com.example.test.notes_history;

public class NotesHistoryResponseModel {

    private String userName;
    private String date;
    private String time;
    private String activityType;
    private String notes;

    public String getNotes() {
        return notes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
