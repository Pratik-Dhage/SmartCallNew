package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.MPinRoomModel;
import com.example.test.roomDB.model.UserNameRoomModel;

@Dao
public interface UserNameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserNameRoomModel userNameRoomModel) ;

    @Update
    void update(UserNameRoomModel userNameRoomModel);

    @Delete
    void delete(UserNameRoomModel userNameRoomModel);

    @Query("SELECT MPinUserName FROM userName_table WHERE UserID =:UserID ")
    String getUserNameUsingUserIDInUserNameRoomDB(String UserID);


    @Query("UPDATE userName_table SET MPinUserName = :newUserName WHERE  UserID = :UserID ")
    void updateUserName(String UserID,String newUserName);
}
