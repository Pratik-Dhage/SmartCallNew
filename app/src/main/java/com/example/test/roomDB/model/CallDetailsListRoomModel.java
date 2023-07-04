package com.example.test.roomDB.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "call_details_table")
public class CallDetailsListRoomModel {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id ;

    @ColumnInfo(name = "fullName")
    private String fullName;  // Customer Full Name

    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber; // Customer Mobile Number

    @ColumnInfo(name = "callDateTime")
    private String callDateTime;

    @ColumnInfo(name = "callDuration")
    private int callDuration;

    @ColumnInfo(name = "notes")
    private String notes;

    //Constructor
    public CallDetailsListRoomModel(String fullName, String phoneNumber, String callDateTime, int callDuration) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.callDateTime = callDateTime;
        this.callDuration = callDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallDateTime() {
        return callDateTime;
    }

    public void setCallDateTime(String callDateTime) {
        this.callDateTime = callDateTime;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
