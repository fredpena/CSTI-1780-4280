package com.pucmm.csti.roomviewmodel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.pucmm.csti.roomviewmodel.database.PersonDao;
import org.jetbrains.annotations.NotNull;

public class PersonViewModelFactory implements ViewModelProvider.Factory {

    private PersonDao personDao;

    public PersonViewModelFactory(PersonDao personDao) {
        this.personDao = personDao;

    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PersonViewModel.class)) {
            return (T) new PersonViewModel(personDao);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
