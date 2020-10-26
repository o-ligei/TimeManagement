package com.example.wowtime.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivityWithAuthActivity extends AppCompatActivity {

    EditText phoneText;
    EditText captchaText;
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_with_auth_activity);
        TextView usePasswordTextView = findViewById(R.id.go_to_another_in_auth);
        usePasswordTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithAuthActivity.this, LoginActivityWithPasswordActivity.class)));
        TextView useRegisterTextView = findViewById(R.id.go_to_register_in_auth);
        useRegisterTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithAuthActivity.this, RegisterActivity.class)));

        phoneText = findViewById(R.id.phone_input_in_auth);
        captchaText = findViewById(R.id.captcha_input_in_auth);

//        Button login = findViewById(R.id.button6);
//        login.setOnClickListener(v -> startActivity(new Intent(LoginActivity1.this, PersonInfo.class)));
        Button btn_get_captcha = findViewById(R.id.btn_captcha_get_in_auth);
        btn_get_captcha.setOnClickListener(v -> OKGetCaptcha());
        btn_login = findViewById(R.id.btn_login_in_auth);
        btn_login.setOnClickListener(v -> OKLoginWitchAuth());
        btn_login.setEnabled(false);
    }

    private void OKGetCaptcha(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("phone",phoneText.getText().toString());//传递键值对参数
                Request request = new Request.Builder().url(InternetConstant.host + "/User/SendCaptchaToPhone").post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    postCaptcha(result);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postCaptcha(String result) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg = null;
                try {
                    msg = jsonObject.get("msg").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                toast.show();
                if(msg == "success")
                    btn_login.setEnabled(true);
            }
        });
    }

    private void OKLoginWitchAuth(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("phone",phoneText.getText().toString());
                formBody.add("captcha",captchaText.getText().toString());
                Request request = new Request.Builder().url(InternetConstant.host + "/User/LoginWithCaptcha").post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    LoginWitchAuth(result);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void LoginWitchAuth(String result ) throws JSONException{
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg = null;
                String str_data = null;
                String userid = null;
                try {
                    msg = jsonObject.get("msg").toString();
                    str_data = jsonObject.get("data").toString();
                    JSONObject data = new JSONObject(str_data);
                    userid = data.get("userid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                toast.show();
                if(msg.equals("success"))
                {
                    UserInfoAfterLogin.userid = Integer.valueOf(userid);
                    Intent intent = new Intent(LoginActivityWithAuthActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

}