package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.CallDetailsListRoomModel;

import java.util.List;

@Dao
public interface CallDetailsListDao {

    @Insert
    void insert(CallDetailsListRoomModel callDetailsListRoomModel);

    @Update
    void update(CallDetailsListRoomModel callDetailsListRoomModel);

    @Delete
    void delete(CallDetailsListRoomModel callDetailsListRoomModel);

    @Query("DELETE FROM call_details_table WHERE phoneNumber =:phoneNumber")
    void deleteCallDetailsListUsingMobileNumber(String phoneNumber);

    @Query("SELECT * FROM call_details_table WHERE phoneNumber =:phoneNumber")
    List<CallDetailsListRoomModel> getCallLogDetailsUsingMobileNumber(String phoneNumber);

}
