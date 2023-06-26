package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.roomDB.model.NotSpokeToCustomerRoomModel;

@Dao
public interface NotSpokeToCustomerDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NotSpokeToCustomerRoomModel notSpokeToCustomerRoomModel);

    @Update
    void update(NotSpokeToCustomerRoomModel notSpokeToCustomerRoomModel);

    @Delete
    void delete(NotSpokeToCustomerRoomModel notSpokeToCustomerRoomModel);

    @Query("SELECT phoneNumber FROM notSpokeToCustomer_table WHERE phoneNumber =:mobileNumber")
    String getMobileNumberWhoNotSpokeWithCustomer(String  mobileNumber);

    @Query("SELECT phoneNumber FROM notSpokeToCustomer_table WHERE fullName =:fullName")
    String getMobileNumberUsingFullNameWhoNotSpokeToCustomer(String fullName);
}
