package com.pucmm.csti.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;
import com.pucmm.csti.model.Product;
@Dao
public interface ProductDao {

    //CRUD
    @Query("SELECT * FROM product ORDER BY itemCode")
    LiveData<List<Product>> findAll();

    @Query("SELECT * FROM product WHERE itemCode = :itemCode")
    Product find(String itemCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete (Product product);
}
