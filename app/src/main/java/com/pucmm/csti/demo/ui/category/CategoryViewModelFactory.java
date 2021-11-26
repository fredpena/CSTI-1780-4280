package com.pucmm.csti.demo.ui.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.pucmm.csti.demo.database.CategoryDao;
import com.pucmm.csti.roomviewmodel.viewmodel.PersonViewModel;
import org.jetbrains.annotations.NotNull;

public class CategoryViewModelFactory implements ViewModelProvider.Factory {

    private final CategoryDao categoryDao;

    public CategoryViewModelFactory(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;

    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(categoryDao);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
