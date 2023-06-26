package com.example.test.roomDB.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notSpokeToCustomer_table")
public class NotSpokeToCustomerRoomModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "notSpokeToCustomer")
    private boolean notSpokeToCustomer;

    @ColumnInfo(name = "fullName")
    private String fullName;  //Customer Full Name

    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber; //Customer Mobile Number


    public NotSpokeToCustomerRoomModel(boolean notSpokeToCustomer, String fullName, String phoneNumber) {
        this.notSpokeToCustomer = notSpokeToCustomer;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNotSpokeToCustomer() {
        return notSpokeToCustomer;
    }

    public void setNotSpokeToCustomer(boolean notSpokeToCustomer) {
        this.notSpokeToCustomer = notSpokeToCustomer;
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
}
