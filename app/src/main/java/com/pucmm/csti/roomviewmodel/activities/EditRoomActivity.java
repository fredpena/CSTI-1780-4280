package com.pucmm.csti.roomviewmodel.activities;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.ActivityEditRoomBinding;
import com.pucmm.csti.roomviewmodel.constants.Constants;
import com.pucmm.csti.roomviewmodel.database.AppDataBase;
import com.pucmm.csti.roomviewmodel.database.AppExecutors;
import com.pucmm.csti.roomviewmodel.database.PersonDao;
import com.pucmm.csti.roomviewmodel.model.Person;

public class EditRoomActivity extends AppCompatActivity {

    private ActivityEditRoomBinding mBinding;
    private EditText name, email, number, pinCode, city;
    private Button save;
    private int personId;
    private Intent intent;
    private PersonDao personDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEditRoomBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        personDao = AppDataBase.getInstance(getApplicationContext()).personDao();
        intent = getIntent();

        personId = intent.getIntExtra(Constants.UPDATE_PERSON, -1);

        initViews();

        if (personId != -1) {
            save.setText("UPDATE");
            AppExecutors.getInstance().diskIO().execute(() -> {
                Person person = personDao.find(personId);
                populateUI(person);
            });
        }


    }

    private void populateUI(Person person) {
        if(person != null){
            mBinding.editName.setText(person.getName());
            mBinding.editEmail.setText(person.getEmail());
            mBinding.editNumber.setText(person.getNumber());
            mBinding.editPincode.setText(person.getPinCode() + "");
            mBinding.editCity.setText(person.getCity());

        }
    }

    private void initViews() {
        name = mBinding.editName;
        email = mBinding.editEmail;
        number = mBinding.editNumber;
        pinCode = mBinding.editPincode;
        city = mBinding.editCity;

        save = mBinding.save;
        save.setOnClickListener(v -> onSaveButtonClicked());
    }

    private void onSaveButtonClicked() {
        Person person = new Person(name.getText().toString().trim(), email.getText().toString().trim(),
                number.getText().toString().trim(), Integer.valueOf(pinCode.getText().toString().trim()),
                city.getText().toString().trim());

        AppExecutors.getInstance().diskIO().execute(() -> {
            if (intent.hasExtra(Constants.UPDATE_PERSON)) {
                person.setId(personId);
                personDao.update(person);
            } else {
                personDao.insert(person);
            }
            finish();
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}