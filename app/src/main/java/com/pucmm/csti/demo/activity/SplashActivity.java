package com.pucmm.csti.demo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pucmm.csti.R;
import com.pucmm.csti.demo.utils.UserSession;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 1000;
    //to get user session data
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Log.e("Splash CheckPoint", "SplashActivity started");
//        session = new UserSession(getApplicationContext());

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String email = "f.ant.pena@gmail.com";
        String password = "p@$$2u0rd";

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Log.e("SplashActivity", "signInWithEmailAndPassword:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SplashActivity", "signInWithEmailAndPassword:failure");
                    }
                });

        //        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("SplashActivity", "createUserWithEmail:success");
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            Log.d("SplashActivity", "user:success: " + user);
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("SplashActivity", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(SplashActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                        // ...
//                    }
//                });

//        /*
//         * Showing splash screen with a timer. This will be useful when you
//         * want to show case your app logo / company
//         */
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity
            //session.checkLogin();

           // if(session.isLoggedIn()){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
           // }


        }, SPLASH_TIME_OUT);
    }
}