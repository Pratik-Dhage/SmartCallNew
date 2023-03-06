package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.CustomerLocationRoomModel;
import com.example.test.roomDB.model.UserLocationRoomModel;

@Dao
public interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CustomerLocationRoomModel customerLocationRoomModel);

    @Update
    void update(CustomerLocationRoomModel customerLocationRoomModel);

    @Query("DELETE FROM customer_location_table where  phoneNumber=:phoneNumber")
    void deleteCustomerData(String phoneNumber);

    @Query("SELECT latitude FROM customer_location_table where phoneNumber=:phoneNumber ")
    String getCustomerLatitude(String phoneNumber);

    @Query("SELECT longitude FROM customer_location_table where phoneNumber=:phoneNumber ")
    String getCustomerLongitude(String phoneNumber);

    @Query("SELECT firstName FROM customer_location_table where phoneNumber=:phoneNumber ")
    String getCustomerName(String phoneNumber);

    @Query("SELECT phoneNumber FROM customer_location_table where firstName=:firstName ")
    String getCustomerPhone(String firstName);

    @Query("SELECT address FROM customer_location_table where phoneNumber=:phoneNumber")
    String getCustomerAddress(String phoneNumber);


}
