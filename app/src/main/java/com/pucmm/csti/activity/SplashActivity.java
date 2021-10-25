package com.pucmm.csti.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pucmm.csti.R;
import com.pucmm.csti.activity.ui.login.LoginActivity;
import com.pucmm.csti.utils.UserSession;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 3000;
    //to get user session data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.e("Splash CheckPoint", "SplashActivity started");

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            Log.e("Handler", "Handler");
        }, SPLASH_TIME_OUT);
    }
}