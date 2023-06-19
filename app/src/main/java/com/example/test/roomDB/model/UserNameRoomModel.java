package com.example.test.roomDB.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userName_table")
public class UserNameRoomModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MPinID")
    private int MPinID ;

    @ColumnInfo(name = "MPin")
    private String MPin;

    @ColumnInfo(name = "UserID")
    private String UserID;

    @ColumnInfo(name = "MPinUserName")
    private String MPinUserName;


    public UserNameRoomModel(String UserID, String MPinUserName) {
        this.UserID = UserID;
        this.MPinUserName = MPinUserName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getMPinID() {
        return MPinID;
    }

    public void setMPinID(int MPinID) {
        this.MPinID = MPinID;
    }

    public String getMPin() {
        return MPin;
    }

    public void setMPin(String MPin) {
        this.MPin = MPin;
    }

    public String getMPinUserName() {
        return MPinUserName;
    }

    public void setMPinUserName(String MPinUserName) {
        this.MPinUserName = MPinUserName;
    }
}
