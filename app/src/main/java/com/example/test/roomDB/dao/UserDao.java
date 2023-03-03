package com.example.test.roomDB.dao;

import androidx.room.Dao;
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

    @Query("SELECT latitude FROM user_location_table where phoneNumber=:phoneNumber ")
    String getUserLatitude(String phoneNumber);

    @Query("SELECT longitude FROM user_location_table where phoneNumber=:phoneNumber ")
    String getUserLongitude(String phoneNumber);

}
