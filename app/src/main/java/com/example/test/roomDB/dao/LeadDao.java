package com.example.test.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.roomDB.model.LeadModel;
import com.example.test.roomDB.model.LeadModelRoom;

import java.util.Collection;
import java.util.List;

@Dao
public interface LeadDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LeadModelRoom leadModelRoom);

    @Update
    void update(LeadModelRoom leadModelRoom);

    @Delete
    void delete(LeadModelRoom leadModelRoom);

    @Query("SELECT DISTINCT * FROM lead_list_table")
    List<LeadModelRoom> getAllLeadListFromRoomDB();
   // LiveData<List<LeadModel>>  getAllLeadListFromRoomDB();

    // to check if table already exists , otherwise it will create new table
    @Query("SELECT name FROM sqlite_master WHERE type='table' AND name=:tableName")
    String checkTableExists(String tableName);

    //to check if Lead already exists
    @Query("SELECT DISTINCT * fROM lead_list_table WHERE leadID =:lead_ID") //leadID is from table
    LeadModel isExisting(String lead_ID); //lead_ID is variable

    //to get count of rows in table
    @Query("SELECT COUNT(*) FROM lead_list_table")
    int getRowCount();

    //to check if data (eg.phone number) is already existing in Table,
    // if Not exists only then data(Api Response) will be added
    @Query("SELECT COUNT(phoneNumber) FROM lead_list_table WHERE phoneNumber =:phone_Number")
    int getCountByPhoneNumber(String phone_Number);
}
