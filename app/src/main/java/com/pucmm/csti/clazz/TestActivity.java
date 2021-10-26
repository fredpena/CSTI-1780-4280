package com.pucmm.csti.clazz;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.pucmm.csti.R;
import com.pucmm.csti.data.dao.UserDao;
import com.pucmm.csti.data.dao.UserDataBase;
import com.pucmm.csti.data.model.User;
import com.pucmm.csti.databinding.ActivityTestBinding;
import com.pucmm.csti.utils.UserSession;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;
    private PrefManager prefManager;

    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(getApplicationContext());

        session = new UserSession(getApplicationContext());

        //UserDao dataBase =  UserDataBase.getInstance(getApplicationContext()).userDao();
        //dataBase.insert(new User());

        //  setContentView(R.layout.activity_test);


        binding.btSend.setOnClickListener(v -> {
//            prefManager.putString("Value", binding.txValue.getText().toString().trim());
//            prefManager.putBoolean("Status", binding.cbSingle.isChecked());
//            prefManager.commit();

//            session.createLoginSession("f.pena");
        });

        binding.btQuery.setOnClickListener(v -> {
            session.logout();
//            String value = prefManager.getString("Value");
//            Boolean status = prefManager.getBoolean("Status");
//
//            if (value != null) {
//                Toast.makeText(TestActivity.this, String.format("%s, esta casado? %s", value.trim(), status), Toast.LENGTH_LONG).show();
//
//            } else {
//                Toast.makeText(TestActivity.this, "La clave no existe", Toast.LENGTH_LONG).show();
//            }

            //   Snackbar.make(v, binding.txValue.getText().toString().trim(), Snackbar.LENGTH_LONG).show();
        });

    }

}