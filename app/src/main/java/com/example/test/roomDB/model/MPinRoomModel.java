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

    @ColumnInfo(name = "UserID")
    private String UserID;

    @ColumnInfo(name = "BranchCode")
    private String BranchCode;


    /*public MPinRoomModel(String mPin, String mPinUserName) {
        this.mPin = mPin;
        this.mPinUserName = mPinUserName;
    }*/

    public MPinRoomModel(String mPin,String UserID , String BranchCode) {
        this.mPin = mPin;
        this.UserID = UserID;
        this.BranchCode = BranchCode;
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }
}
