package com.pucmm.csti.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

import com.pucmm.csti.model.Carousel;
import com.pucmm.csti.model.Product;
import com.pucmm.csti.model.relationships.ProductWithCarousel;

@Dao
public interface ProductDao {

    //CRUD
    @Query("SELECT * FROM product ORDER BY itemCode")
    LiveData<List<ProductWithCarousel>> findAll();


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Product product);

    @Query("DELETE FROM carousel WHERE product = :uid")
    void deleteCarousels(int uid);

    @Insert
    void insertCarousels(List<Carousel> carousels);

    @Transaction
    @Update
    void update(Product product);

    @Update
    void updateCarousels(List<Carousel> carousels);

    @Transaction
    @Delete
    void delete (Product product);

    @Delete
    void deleteCarousels(List<Carousel> carousels);

}
