package com.pucmm.csti.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pucmm.csti.databinding.ActivityForgotPasswordBinding;


public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(view -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });

        binding.register.setOnClickListener(view -> {
            startActivity(new Intent(ForgotPasswordActivity.this, RegisterActivity.class));
            finish();
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptForgotPassword();
            }
        });
    }

    private void attemptForgotPassword() {
        Toast.makeText(ForgotPasswordActivity.this, "COMING SOON!!!", Toast.LENGTH_SHORT).show();
    }
}