package com.example.wowtime.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;

public class LoginActivityWithPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_with_password_activity);
        TextView useCaptchaTextView = findViewById(R.id.textView7);
        useCaptchaTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithPasswordActivity.this, LoginActivityWithAuthActivity.class)));
        TextView useRegisterTextView = findViewById(R.id.textView6);
        useRegisterTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithPasswordActivity.this, RegisterActivity.class)));
    }
}