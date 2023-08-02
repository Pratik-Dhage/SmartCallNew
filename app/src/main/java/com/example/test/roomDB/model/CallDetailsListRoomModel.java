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

    @ColumnInfo(name = "attemptNo")
    private int attemptNo;

    @ColumnInfo(name = "dataSetId")
    private String dataSetId;

    //Constructor
    //Only Used in NotSpokeToCustomerActivity to store call details
    public CallDetailsListRoomModel( String callDateTime, int callDuration, int attemptNo, String dataSetId) {
        this.callDateTime = callDateTime;
        this.callDuration = callDuration;
        this.attemptNo = attemptNo;
        this.dataSetId = dataSetId;
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

    public int getAttemptNo() {
        return attemptNo;
    }

    public void setAttemptNo(int attemptNo) {
        this.attemptNo = attemptNo;
    }

    public String getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(String dataSetId) {
        this.dataSetId = dataSetId;
    }
}
