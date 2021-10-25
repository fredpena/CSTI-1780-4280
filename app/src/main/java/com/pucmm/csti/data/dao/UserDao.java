package com.pucmm.csti.data.dao;

import androidx.room.*;
import com.pucmm.csti.data.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE user_name = :userName AND password = :password")
    User login(String userName, String password);

    @Insert
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    public void updateAll(User... users);

    @Update
    public void update(User user);

    @Delete
    void delete(User user);
}
