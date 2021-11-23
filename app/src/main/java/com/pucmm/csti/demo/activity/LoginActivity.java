package com.pucmm.csti.demo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.ActivityLoginBinding;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.retrofit.UserApiService;
import com.pucmm.csti.demo.utils.ConstantsUtil;
import com.pucmm.csti.demo.utils.UserSession;
import com.pucmm.csti.demo.utils.ValidUtil;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Userr.class, dateJsonDeserializer);

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsUtil.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build();

            final Call<Userr> userCall = retrofit.create(UserApiService.class).login(user);
            userCall.enqueue(new Callback<Userr>() {
                @Override
                public void onResponse(Call<Userr> call, Response<Userr> response) {
                    progressDialog.dismiss();
                    switch (response.code()) {
                        case 200:
                            session.createLoginSession(response.body());

                            FancyToast.makeText(LoginActivity.this, "Successfully login", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            break;
                        default:
                            try {
                                FancyToast.makeText(LoginActivity.this, response.errorBody().string(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }

                @Override
                public void onFailure(Call<Userr> call, Throwable error) {
                    progressDialog.dismiss();
                    FancyToast.makeText(LoginActivity.this, error.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        }
    }

    private final JsonDeserializer<Userr> dateJsonDeserializer = (json, typeOfT, context) -> {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        final JsonObject jo = (JsonObject) json;

        return new Userr()
                .setUid(jo.get("uid").getAsInt())
                .setFirstName(jo.get("firstName").getAsString())
                .setLastName(jo.get("lastName").getAsString())
                .setEmail(jo.get("email").getAsString())
                .setRol(Userr.ROL.valueOf(jo.get("rol").getAsString()))
                .setContact(jo.get("contact").getAsString())
                .setPhoto(jo.get("photo").getAsString())
                .setBirthday(dateFormat.format(new Date(jo.get("birthday").getAsLong())));
    };

}