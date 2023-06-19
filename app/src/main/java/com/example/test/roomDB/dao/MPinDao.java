package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.MPinRoomModel;

@Dao
public interface MPinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MPinRoomModel mPinRoomModel);

    @Update
    void update(MPinRoomModel mPinRoomModel);

    @Delete
    void delete(MPinRoomModel mPinRoomModel);

    @Query("SELECT mPin FROM mpin_table WHERE UserID = :UserID")
    String getMPinFromRoomDB(String UserID);

    @Query("SELECT mPinUserName FROM mpin_table WHERE mPin =:mPin ")
    String getUserNameUsingMPinInRoomDB(String mPin);

    @Query("SELECT mPinUserName FROM mpin_table WHERE UserID = :UserID")
    String getUserNameUsingUserID(String UserID);

    @Query("UPDATE mpin_table SET mPin = :newMPin WHERE  UserID = :UserID AND BranchCode = :BranchCode")
    void updateMPin(String newMPin , String UserID , String BranchCode);

    @Query("SELECT COUNT(*) FROM mpin_table")
    int checkAnyMPinExists();

    @Query("SELECT UserID FROM mpin_table WHERE mPin = :mPin")
    String getUserID(String mPin);

    @Query("SELECT BranchCode FROM mpin_table WHERE mPin = :mPin")
    String getBranchCode(String mPin);

    @Query("SELECT UserID FROM mpin_table ")
     String getUserID();
}
