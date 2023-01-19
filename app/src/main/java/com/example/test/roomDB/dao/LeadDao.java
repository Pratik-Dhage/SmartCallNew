package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.LeadModel;

import java.util.List;

@Dao
public interface LeadDao {


    @Insert
    void insert(LeadModel leadModel);

    @Update
    void update(LeadModel leadModel);

    @Delete
    void delete(LeadModel leadModel);

    @Query("SELECT * FROM leadmodel")
    List<LeadModel> getAllLeadListFromRoomDB();

}
