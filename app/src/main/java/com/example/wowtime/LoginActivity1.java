package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        TextView usePasswordTextView = findViewById(R.id.textView4);
        usePasswordTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity1.this, LoginActivity2.class)));
        TextView useRegisterTextView = findViewById(R.id.textView5);
        useRegisterTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity1.this, RegisActivity.class)));
    }
}