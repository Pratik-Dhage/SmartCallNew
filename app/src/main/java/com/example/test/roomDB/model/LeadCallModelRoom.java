package com.example.test.roomDB.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lead_call_table")
public class LeadCallModelRoom {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "leadCalls")
    private int leadCalls ; // This will Store Total generated Lead Calls to a particular Phone Number

    @ColumnInfo(name = "firstName")
    private String firstName;  //Lead First Name
    @ColumnInfo(name = "lastName")
    private String lastName;   //Lead Last Name
    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber; //Lead Mobile Number

    public LeadCallModelRoom(int leadCalls, String firstName, String phoneNumber) {
        this.leadCalls = leadCalls;
        this.firstName = firstName;
       // this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getLeadCalls() {
        return leadCalls;
    }

    public void setLeadCalls(int leadCalls) {
        this.leadCalls = leadCalls;
    }
}
