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
}
