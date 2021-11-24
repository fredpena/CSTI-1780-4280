package com.pucmm.csti.demo.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.pucmm.csti.demo.model.Category;
import com.pucmm.csti.roomviewmodel.model.Person;

import java.util.List;

@Dao
public interface CategoryDao {

    //CRUD
    @Query("SELECT * FROM category ORDER BY uid")
    LiveData<List<Category>> findAll();

    @Query("SELECT * FROM category WHERE uid = :uid")
    Category find(int uid);

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete (Category category);
}
