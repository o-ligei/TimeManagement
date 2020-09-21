package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        TextView useCaptchaTextView = findViewById(R.id.textView7);
        useCaptchaTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity2.this, LoginActivity1.class)));
        TextView useRegisterTextView = findViewById(R.id.textView6);
        useRegisterTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity2.this, RegisActivity.class)));
    }
}