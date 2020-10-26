package com.example.wowtime.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.util.InternetConstant;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EmailActivateActivity extends AppCompatActivity {
    TextView emailInput;
    TextView captchaInput;
    String phone;
    Button btn_handson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_activate);

        emailInput = findViewById(R.id.email_input_in_EA);
        captchaInput = findViewById(R.id.captcha_input_in_EA);

        phone = getIntent().getStringExtra("phone");

        Button btn_get_captcha = findViewById(R.id.btn_captcha_in_EA);
        btn_get_captcha.setOnClickListener(v -> OKGetCaptcha());

        btn_handson = findViewById(R.id.btn_handson_in_EA);
        btn_handson.setEnabled(false);
        btn_handson.setOnClickListener(v -> OkHandsOn());
    }

    void OKGetCaptcha(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("phone", phone);
                formBody.add("email",emailInput.getText().toString());
                Request request = new Request.Builder().url(InternetConstant.host + "/User/SendCaptchaToEmail").post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    GetCaptcha();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void GetCaptcha(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_handson.setEnabled(true);
            }
        });
    }

    void OkHandsOn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("phone", phone);
                formBody.add("email",emailInput.getText().toString());
                formBody.add("captcha",captchaInput.getText().toString());
                Request request = new Request.Builder().url(InternetConstant.host + "/User/ActivateEmail").post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    HandsOn(result);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void HandsOn(String result) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                String msg = "";
                try {
                    jsonObject = new JSONObject(result);
                    msg = jsonObject.get("msg").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                toast.show();
                if(msg.equals("success")){
                    Intent intent = null;
                    intent = new Intent(EmailActivateActivity.this,PersonInfoFragment.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}