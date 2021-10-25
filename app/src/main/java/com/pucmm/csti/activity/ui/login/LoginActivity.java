package com.pucmm.csti.activity.ui.login;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.csti.R;
import com.pucmm.csti.data.dao.UserDataBase;
import com.pucmm.csti.databinding.ActivityLoginBinding;
import com.pucmm.csti.utils.UserSession;
import com.pucmm.csti.utils.ValidUtil;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private UserSession session;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new UserSession(getApplicationContext());

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        UserDataBase userDataBase = UserDataBase.getInstance(this);
        System.out.println(userDataBase.userDao().getAll());

    }


}