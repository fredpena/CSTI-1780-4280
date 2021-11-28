package com.pucmm.csti.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.pucmm.csti.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    //CRUD
    @Query("SELECT * FROM category ORDER BY uid")
    LiveData<List<Category>> findAll();
//
//    @Query("SELECT * FROM category WHERE active = 'TRUE' ORDER BY uid")
//    List<Category> findAllActive();

    @Query("SELECT * FROM category WHERE uid = :uid")
    Category find(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete (Category category);
}
