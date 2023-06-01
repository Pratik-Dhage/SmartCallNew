package com.example.test.roomDB.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mPin_table")
public class MPinRoomModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mPinID")
    private int mPinID ;

    @ColumnInfo(name = "mPin")
    private String mPin;

    @ColumnInfo(name = "mPinUserName")
    private String mPinUserName;

    public MPinRoomModel(String mPin, String mPinUserName) {
        this.mPin = mPin;
        this.mPinUserName = mPinUserName;
    }

    public int getMPinID() {
        return mPinID;
    }

    public void setMPinID(int mPinID) {
        this.mPinID = mPinID;
    }

    public String getMPin() {
        return mPin;
    }

    public void setMPin(String mPin) {
        this.mPin = mPin;
    }

    public String getMPinUserName() {
        return mPinUserName;
    }

    public void setMPinUserName(String mPinUserName) {
        this.mPinUserName = mPinUserName;
    }
}
