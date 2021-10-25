package com.pucmm.csti.data.dao;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.pucmm.csti.data.model.User;

@Database(entities = User.class, exportSchema = false, version = 1)
public abstract class UserDataBase extends RoomDatabase {
    private static final String DB_NAME = "user_db";
    private static UserDataBase instance;

    public static synchronized UserDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDataBase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;

    }

    public abstract UserDao userDao();


}
