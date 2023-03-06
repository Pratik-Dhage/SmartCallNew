package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.UserLocationRoomModel;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserLocationRoomModel userLocationRoomModel);

    @Update
    void update(UserLocationRoomModel userLocationRoomModel);

    @Query("DELETE FROM user_location_table where  phoneNumber=:phoneNumber")
    void deleteUserData(String phoneNumber);

    @Query("SELECT latitude FROM user_location_table where phoneNumber=:phoneNumber ")
    String getUserLatitude(String phoneNumber);

    @Query("SELECT longitude FROM user_location_table where phoneNumber=:phoneNumber ")
    String getUserLongitude(String phoneNumber);

    @Query("SELECT firstName FROM user_location_table where phoneNumber=:phoneNumber ")
    String getUserName(String phoneNumber);

    @Query("SELECT phoneNumber FROM user_location_table where firstName=:firstName ")
    String getUserPhone(String firstName);

    @Query("SELECT address FROM user_location_table where phoneNumber=:phoneNumber")
    String getUserAddress(String phoneNumber);

}
