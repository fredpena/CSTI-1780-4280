package com.pucmm.csti.roomviewmodel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pucmm.csti.roomviewmodel.database.AppDataBase;
import com.pucmm.csti.roomviewmodel.database.PersonDao;
import com.pucmm.csti.roomviewmodel.model.Person;

import java.util.List;

public class PersonViewModel extends ViewModel {
    private LiveData<List<Person>> personListLiveData;

    public PersonViewModel(@NonNull PersonDao personDao){

        personListLiveData = personDao.findAll();
    }

    public LiveData<List<Person>> getPersonListLiveData() {
        return personListLiveData;
    }
}
