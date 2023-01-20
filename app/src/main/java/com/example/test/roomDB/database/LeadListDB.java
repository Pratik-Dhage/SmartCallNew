package com.example.test.roomDB.database;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.test.roomDB.dao.LeadDao;
import com.example.test.roomDB.model.LeadModel;
import com.example.test.roomDB.model.LeadModelRoom;

import kotlin.jvm.Synchronized;

@Database(entities = {LeadModelRoom.class},version = 1)
public abstract class LeadListDB extends RoomDatabase {

    public abstract LeadDao leadDao();

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
