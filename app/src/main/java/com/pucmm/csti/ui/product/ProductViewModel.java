package com.pucmm.csti.ui.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pucmm.csti.database.AppDataBase;
import com.pucmm.csti.model.Product;
import com.pucmm.csti.model.relationships.ProductWithCarousel;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private final LiveData<List<ProductWithCarousel>> listLiveData;


    public ProductViewModel(@NonNull AppDataBase dataBase) {

        listLiveData = dataBase.productDao().findAll();
    }

    public LiveData<List<ProductWithCarousel>> getListLiveData() {
        return listLiveData;
    }


}
