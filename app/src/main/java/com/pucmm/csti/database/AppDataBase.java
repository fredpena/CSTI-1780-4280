package com.pucmm.csti.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.pucmm.csti.model.Carousel;
import com.pucmm.csti.model.Category;
import com.pucmm.csti.model.Product;

@Database(entities = {Category.class, Product.class, Carousel.class}, version = 3)
public abstract class AppDataBase extends RoomDatabase {
    private static final String DATABASE_NAME = "csti";
    private static final Object LOCK = new Object();
    private static AppDataBase sIntance;

    public static AppDataBase getInstance(Context context) {
        if (sIntance == null) {
            synchronized (LOCK) {
                sIntance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sIntance;
    }

    public abstract CategoryDao categoryDao();

    public abstract ProductDao productDao();


}
