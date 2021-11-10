package com.pucmm.csti.roomviewmodel.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.pucmm.csti.roomviewmodel.model.Person;

@Database(entities = {Person.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static final String DATABASE_NAME = "csti";
    private static final Object LOCK = new Object();
    private static AppDataBase sIntance;

    public static AppDataBase getInstance(Context context) {
        if (sIntance == null) {
            synchronized (LOCK) {
                sIntance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DATABASE_NAME).build();
            }
        }
        return sIntance;
    }

    public abstract PersonDao personDao();


}
