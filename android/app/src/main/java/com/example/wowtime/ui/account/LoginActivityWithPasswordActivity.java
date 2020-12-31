package com.example.wowtime.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.MainApplication;
import com.example.wowtime.R;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import com.example.wowtime.websocket.TWebSocketClientService;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivityWithPasswordActivity extends AppCompatActivity {

    EditText phoneText;
    EditText passwordText;
    TextView forgetPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainApplication.getThemeNumber() == 1) { setTheme(R.style.DarkTheme); }
        setContentView(R.layout.login_with_password_activity);
        TextView useCaptchaTextView = findViewById(R.id.textView7);
        useCaptchaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent signUpIntent = new Intent(LoginActivityWithPasswordActivity.this,
                                                 LoginActivityWithAuthActivity.class);
                startActivity(signUpIntent);
            }
        });
//        useCaptchaTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithPasswordActivity.this, LoginActivityWithAuthActivity.class)));
        TextView useRegisterTextView = findViewById(R.id.GameTitle);
        useRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent signUpIntent = new Intent(LoginActivityWithPasswordActivity.this,
                                                 TaskSuccessActivity.class);
                startActivity(signUpIntent);
            }
        });
//        useRegisterTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithPasswordActivity.this, RegisterActivity.class)));

        phoneText = findViewById(R.id.phone_input_in_pass);
        passwordText = findViewById(R.id.password_input_in_pass);
        forgetPasswordText = findViewById(R.id.text_forget_password);
        forgetPasswordText.setOnClickListener(v -> startActivity(
                new Intent(LoginActivityWithPasswordActivity.this, CaptchaConfirmActivity.class)
                        .putExtra("target", "password")));

        Button btn_login = findViewById(R.id.btn_login_in_pass);
        btn_login.setOnClickListener(v -> OKLoginWitchPass());

    }

    private void OKLoginWitchPass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("phone", phoneText.getText().toString());
                formBody.add("password", passwordText.getText().toString());
                Request request = new Request.Builder()
                        .url(InternetConstant.host + "/User/LoginWithPassword")
                        .post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    LoginWitchPass(result);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void LoginWitchPass(String result) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                String msg = null;
                String str_data = null;
                String str_user = null;
                String userid = null;
                String username = null;

                try {
                    jsonObject = JSONObject.parseObject(result);
                    msg = Objects.requireNonNull(jsonObject.get("msg")).toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                toast.show();
                assert msg != null;
                if (msg.equals("success")) {
                    str_data = Objects.requireNonNull(jsonObject.get("data")).toString();
                    JSONObject data = JSONObject.parseObject(str_data);
                    str_user = Objects.requireNonNull(data.get("user")).toString();
                    JSONObject user = JSONObject.parseObject(str_user);
                    userid = Objects.requireNonNull(user.get("userId")).toString();
//                    UserInfoAfterLogin.userid = Integer.valueOf(userid);
                    username = user.get("username").toString();
//                    UserInfoAfterLogin.username = username;
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    Editor editor = sharedPreferences.edit();
//                    UserInfoAfterLogin.userid = Integer.valueOf(userid);
                    editor.putInt("userId",Integer.valueOf(userid));
                    editor.putString("userName",username);
                    editor.apply();
                    UserInfoAfterLogin.userid = Integer.valueOf(userid);
                    UserInfoAfterLogin.username = username;
//                    UserInfoAfterLogin.username = username;
                    Intent startIntent = new Intent(LoginActivityWithPasswordActivity.this,
                                                    TWebSocketClientService.class);
                    startService(startIntent);
                    Intent intent = new Intent(LoginActivityWithPasswordActivity.this,
                                               MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        return super.onCreateOptionsMenu(menu);
    }

}