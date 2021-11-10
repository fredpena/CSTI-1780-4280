package com.pucmm.csti.roomviewmodel.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.pucmm.csti.roomviewmodel.model.Person;

import java.util.List;

@Dao
public interface PersonDao {
    //CRUD
    @Query("SELECT * FROM person ORDER BY id")
    LiveData<List<Person>> findAll();

    @Query("SELECT * FROM person WHERE id = :id")
    Person find(int id);

    @Insert
    void insert(Person person);

    @Update
    void update(Person person);

    @Delete
    void delete (Person person);
}
