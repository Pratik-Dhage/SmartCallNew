package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.LeadCallModelRoom;

@Dao
public interface LeadCallDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LeadCallModelRoom leadCallModelRoom);

    @Update
    void update(LeadCallModelRoom leadCallModelRoom);

    //To get count of Calls made to the particular Phone Number
    @Query("SELECT leadCalls FROM lead_call_table WHERE phoneNumber =:phone_Number")
    int getCallCountUsingPhoneNumber(String phone_Number);

    @Query("UPDATE lead_call_table SET leadCalls=:leadsCount where phoneNumber =:phoneNumber")
     void UpdateLeadCalls(int leadsCount,String phoneNumber);


    @Query("SELECT firstName FROM lead_call_table WHERE phoneNumber =:phone_Number")
    String getFullNameUsingPhoneNumber(String phone_Number);


    // *** Call Counts Based On Unique dataSetId *** //

    @Query("SELECT leadCalls FROM lead_call_table WHERE dataSetId =:dataSetId")
    int getCallCountUsingDataSetId(String dataSetId);

    @Query("UPDATE lead_call_table SET leadCalls=:leadsCount where dataSetId =:dataSetId")
    void UpdateLeadCallsUsingDataSetId(int leadsCount,String dataSetId);
}
