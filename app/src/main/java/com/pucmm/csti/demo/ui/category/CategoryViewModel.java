package com.pucmm.csti.demo.ui.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pucmm.csti.demo.database.CategoryDao;
import com.pucmm.csti.demo.model.Category;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final LiveData<List<Category>> listLiveData;

    public CategoryViewModel(@NonNull CategoryDao categoryDao) {

        listLiveData = categoryDao.findAll();
    }

    public LiveData<List<Category>> getListLiveData() {
        return listLiveData;
    }
}
