package com.example.test.roomDB.model;

//This is a Model Class for Room DB

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "lead_list_table")
public class LeadModel {

    @ColumnInfo(name = "firstName")
    private String firstName;  //Lead First Name
    @ColumnInfo(name = "lastName")
    private String lastName;   //Lead Last Name
    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber; //Lead Mobile Number

    public LeadModel(String firstName, String phoneNumber) {
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
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
}