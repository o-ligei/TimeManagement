package com.example.wowtime.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import java.io.IOException;

import kotlin.ranges.CharProgression;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView useNameTextView;
    TextView phoneTextView;
    TextView passwordTextView;
    TextView captchaTextView;
    Button btn_register;
    TextView usernameWarning;
    TextView passwordWaring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        useNameTextView = findViewById(R.id.username_input);
        phoneTextView = findViewById(R.id.phone_input);
        passwordTextView = findViewById(R.id.passsword_input);
        captchaTextView = findViewById(R.id.captcha_input);
        usernameWarning = findViewById(R.id.username_warning);
        passwordWaring = findViewById(R.id.password_warning);

        usernameWarning.setVisibility(View.INVISIBLE);
        passwordWaring.setVisibility(View.INVISIBLE);
        Button btn_getCaptcha = findViewById(R.id.btn_getCaptcha);
//        btn_getCaptcha.setEnabled(false);
//
//        if(phoneTextView.getText().toString().matches(telRegex))
//            btn_getCaptcha.setEnabled(true);
        btn_getCaptcha.setOnClickListener(v -> OKGetCaptcha());
        btn_register = findViewById(R.id.btn_register);
//        btn_register.setOnClickListener(v -> OKRegister());
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OKRegister();
            }
        });
        btn_register.setEnabled(false);
    }

    private void OKGetCaptcha() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("phone", phoneTextView.getText().toString());//传递键值对参数
                Request request = new Request.Builder()
                        .url(InternetConstant.host + "/User/SendCaptchaToPhone")
                        .post(formBody.build()).build();
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
                String msg = null;
                jsonObject = JSONObject.parseObject(result);
                msg = jsonObject.get("msg").toString();
                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                toast.show();
                if (msg.equals("success")) { btn_register.setEnabled(true); }
            }
        });
    }

    private void OKRegister() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("username", useNameTextView.getText().toString());
        formBody.add("password", passwordTextView.getText().toString());
        formBody.add("phone", phoneTextView.getText().toString());//传递键值对参数
        formBody.add("captcha", captchaTextView.getText().toString());

        Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
//                JSONObject jsonObject = null;
                String msg = message.getData().get("msg").toString();
//                try {
////                    jsonObject = new JSONObject(Boolean.parseBoolean(result));
//                    jsonObject = JSONObject.parseObject(result);
//                    System.out.println(jsonObject);
//                    msg = jsonObject.get("msg").toString();
//                    System.out.println(msg);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                toast.show();
                if (msg.equals("success")) {
                    Intent intent = new Intent(RegisterActivity.this,
                                               LoginActivityWithPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            return false;
        });

        Ajax ajax = new Ajax("/User/Register", formBody, handler, InternetConstant.FETCH);
        ajax.fetch();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//                formBody.add("username", useNameTextView.getText().toString());
//                formBody.add("password", passwordTextView.getText().toString());
//                formBody.add("phone", phoneTextView.getText().toString());//传递键值对参数
//                formBody.add("captcha", captchaTextView.getText().toString());
//                Request request = new Request.Builder()
//                        .url(InternetConstant.host + "/User/Register").post(formBody.build())
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();//发送请求
//                    String result = response.body().string();
//                    System.out.println(result);
//                    postRegister(result);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

//    private void postRegister(String result) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                JSONObject jsonObject = null;
//                String msg = null;
//                try {
////                    jsonObject = new JSONObject(Boolean.parseBoolean(result));
//                    jsonObject = JSONObject.parseObject(result);
//                    System.out.println(jsonObject);
//                    msg = jsonObject.get("msg").toString();
//                    System.out.println(msg);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
//                toast.show();
//                if (msg.equals("success")) {
//                    Intent intent = new Intent(RegisterActivity.this,
//                                               LoginActivityWithPasswordActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        return super.onCreateOptionsMenu(menu);
    }
}
