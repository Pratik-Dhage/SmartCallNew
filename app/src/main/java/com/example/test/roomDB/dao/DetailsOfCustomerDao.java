package com.example.test.roomDB.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.DetailsOfCustomerRoomModel;
import com.example.test.roomDB.model.LeadModelRoom;

import java.util.List;

public interface DetailsOfCustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DetailsOfCustomerRoomModel detailsOfCustomerRoomModel);

    @Update
    void update(DetailsOfCustomerRoomModel detailsOfCustomerRoomModel);

    @Delete
    void delete(DetailsOfCustomerRoomModel detailsOfCustomerRoomModel);

     @Query("SELECT DISTINCT * FROM details_of_customer_table")
    List<DetailsOfCustomerRoomModel> getAllDetailsOfCustomerFromRoomDB();

}
