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

public class LoginActivityWithPasswordActivity extends AppCompatActivity {

    EditText phoneText;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_with_password_activity);
        TextView useCaptchaTextView = findViewById(R.id.textView7);
        useCaptchaTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithPasswordActivity.this, LoginActivityWithAuthActivity.class)));
        TextView useRegisterTextView = findViewById(R.id.GameTitle);
        useRegisterTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivityWithPasswordActivity.this, RegisterActivity.class)));

        phoneText = findViewById(R.id.phone_input_in_pass);
        passwordText = findViewById(R.id.password_input_in_pass);

        Button btn_login = findViewById(R.id.btn_login_in_pass);
        btn_login.setOnClickListener(v -> OKLoginWitchPass());
    }

    private void OKLoginWitchPass(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("phone",phoneText.getText().toString());
                formBody.add("password",passwordText.getText().toString());
                Request request = new Request.Builder().url(InternetConstant.host + "/User/LoginWithPassword").post(formBody.build()).build();
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

    private void LoginWitchPass(String result ) throws JSONException{
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
                    Intent intent = new Intent(LoginActivityWithPasswordActivity.this, MainActivity.class);
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