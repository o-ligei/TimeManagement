package com.example.wowtime.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;
import com.example.wowtime.util.UserInfoAfterLogin;

public class LoginActivityWithAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_with_auth_activity);
        TextView usePasswordTextView = findViewById(R.id.textView4);
        usePasswordTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithAuthActivity.this, LoginActivityWithPasswordActivity.class)));
        TextView useRegisterTextView = findViewById(R.id.textView5);
        useRegisterTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithAuthActivity.this, RegisterActivity.class)));
//        Button login = findViewById(R.id.button6);
//        login.setOnClickListener(v -> startActivity(new Intent(LoginActivity1.this, PersonInfo.class)));
    }
}