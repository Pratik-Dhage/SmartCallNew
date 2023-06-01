package com.example.test.roomDB.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.test.roomDB.dao.CustomerDao;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.LeadDao;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserDao;
import com.example.test.roomDB.model.CustomerLocationRoomModel;
import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.roomDB.model.LeadModelRoom;
import com.example.test.roomDB.model.MPinRoomModel;
import com.example.test.roomDB.model.UserLocationRoomModel;

import kotlin.jvm.Synchronized;

@Database(entities = {LeadModelRoom.class, LeadCallModelRoom.class, UserLocationRoomModel.class, CustomerLocationRoomModel.class, MPinRoomModel.class},version = 6)
public abstract class LeadListDB extends RoomDatabase {

    public abstract LeadDao leadDao();
    public abstract LeadCallDao leadCallDao();
    public abstract UserDao userDao();
    public abstract CustomerDao customerDao();
    public abstract MPinDao mPinDao();

    private static LeadListDB leadListDBInstance= null; // the instance will be created only once

    private static String DATABASE_NAME = "LeadList_db";

    @Synchronized // this will allow only one instance throughout the app
  public  static LeadListDB getInstance(Context context){


        if (leadListDBInstance == null) {
            leadListDBInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    LeadListDB.class,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }


            return  leadListDBInstance;
}

}
