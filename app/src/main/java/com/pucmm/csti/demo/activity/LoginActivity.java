package com.pucmm.csti.demo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.ActivityLoginBinding;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.retrofit.UserApiService;
import com.pucmm.csti.demo.utils.ConstantsUtil;
import com.pucmm.csti.demo.utils.UserSession;
import com.pucmm.csti.demo.utils.ValidUtil;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

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


        binding.email.setText("fr.pena@ce.pucmm.edu.do");
        binding.password.setText("123456");

        //if user wants to register
        binding.registerNow.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        binding.forgotPass.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            finish();
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {

        if (ValidUtil.isEmpty(this, this.binding.email, this.binding.password)) {
            return;
        }
        // Store values at the time of the login attempt.
        final String email = this.binding.email.getText().toString();
        final String password = this.binding.password.getText().toString();

        if (ValidUtil.isEmailValid(this.binding.email, email) && ValidUtil.isPasswordValid(this.binding.password, password)) {
            final KProgressHUD progressDialog = KProgressHUD.create(LoginActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(getString(R.string.please_wait))
                    .setDetailsLabel(getString(R.string.authenticating))
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

            final JsonObject user = new JsonObject();
            user.addProperty("email", binding.email.getText().toString().trim());
            user.addProperty("password", binding.password.getText().toString().trim());

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsUtil.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final Call<Userr> userCall = retrofit.create(UserApiService.class).login(user);
            userCall.enqueue(new Callback<Userr>() {
                @Override
                public void onResponse(Call<Userr> call, Response<Userr> response) {
                    progressDialog.dismiss();
                    switch (response.code()) {
                        case 200:
                            Toast.makeText(LoginActivity.this, "Successfully login", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                            break;
                        default:
                            try {
                                Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }

                @Override
                public void onFailure(Call<Userr> call, Throwable error) {
                    progressDialog.dismiss();

                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}