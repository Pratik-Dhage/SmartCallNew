package com.example.test.roomDB.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_location_table")
public class UserLocationRoomModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id ;

    @ColumnInfo(name = "firstName")
    private String firstName;  //User First Name
    @ColumnInfo(name = "lastName")
    private String lastName;   //User Last Name
    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber; //User Mobile Number

    @ColumnInfo(name = "latitude")
    private String latitude; //User Latitude

    @ColumnInfo(name = "longitude")
    private String longitude; //User Longitude

    @ColumnInfo(name="address")
    private String userAddress; // User Complete Address

    @Ignore
    public UserLocationRoomModel(String firstName, String lastName, String phoneNumber,String latitude,String longitude) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.latitude=latitude;
        this.longitude=longitude;

    }

    public UserLocationRoomModel(String firstName, String lastName, String phoneNumber, String userAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userAddress = userAddress;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
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


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}


