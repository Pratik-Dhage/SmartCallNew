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

    @Query("SELECT callDateTime FROM call_details_table WHERE phoneNumber =:phoneNumber")
    String getCallDateTimeUsingMobileNumber(String phoneNumber);

    //id is needed because data is being fetched for same mobileNumber . So id is used to differentiate items in List
    @Query("SELECT id,callDateTime,callDuration,attemptNo FROM call_details_table WHERE phoneNumber =:phoneNumber")
    List<CallDetailsListRoomModel> getCallLogDetailsUsingMobileNumber(String phoneNumber);

    @Query("UPDATE call_details_table SET attemptNo=:attemptNo where phoneNumber =:phoneNumber")
    void UpdateAttemptNo(int attemptNo,String phoneNumber);

    //*** CallDetailsList Using dataSetId *** //

    @Query("DELETE FROM call_details_table WHERE dataSetId =:dataSetId")
    void deleteCallDetailsListUsingDataSetId(String dataSetId);

    @Query("SELECT callDateTime FROM call_details_table WHERE dataSetId =:dataSetId")
    String getCallDateTimeUsingDataSetId(String dataSetId);

    @Query("SELECT id,callDateTime,callDuration,attemptNo FROM call_details_table WHERE dataSetId =:dataSetId")
    List<CallDetailsListRoomModel> getCallLogDetailsUsingDataSetId(String dataSetId);

    @Query("UPDATE call_details_table SET attemptNo=:attemptNo where dataSetId =:dataSetId")
    void UpdateAttemptNoUsingDataSetId(int attemptNo,String dataSetId);

    @Query("SELECT COUNT(*) FROM call_details_table WHERE dataSetId =:dataSetId")
    int getCountOfCallDetailsListUsingDataSetId(String dataSetId);

}
