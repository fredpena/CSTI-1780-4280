package com.pucmm.csti.ui.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pucmm.csti.database.AppDataBase;
import com.pucmm.csti.database.CategoryDao;
import com.pucmm.csti.model.Category;
import com.pucmm.csti.model.Userr;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final LiveData<List<Category>> listLiveData;


    public CategoryViewModel(@NonNull AppDataBase dataBase) {

        listLiveData = dataBase.categoryDao().findAll();
    }

    public LiveData<List<Category>> getListLiveData() {
        return listLiveData;
    }
}
