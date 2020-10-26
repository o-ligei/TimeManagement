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

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CaptchaConfirmActivity extends AppCompatActivity {

    TextView phoneInputText;
    TextView captchaConfirmText;
    Button btn_handson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha_confirm);

        phoneInputText = findViewById(R.id.phone_input_in_pass);
        captchaConfirmText = findViewById(R.id.cap_input_in_cap);

        Button btn_getCaptcha = findViewById(R.id.get_cap_in_cap);
        btn_getCaptcha.setOnClickListener(v -> OKGetCaptcha());

        btn_handson = findViewById(R.id.handson_in_cap);
        btn_handson.setEnabled(false);
        btn_handson.setOnClickListener(v -> GoToNext());
    }

    void OKGetCaptcha(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("phone",phoneInputText.getText().toString());//传递键值对参数
                Request request = new Request.Builder().url(InternetConstant.host + "/User/SendCaptchaToPhone").post(formBody.build()).build(); //TODO:change the url
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    btn_handson.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void GoToNext(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("phone",phoneInputText.getText().toString());
                formBody.add("captcha",captchaConfirmText.getText().toString());
                Request request = new Request.Builder().url(InternetConstant.host + "/User/SendCaptchaToPhone").post(formBody.build()).build(); //TODO:change the url
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    btn_handson.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void GoToNext(String result) throws JSONException {
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
                if(msg == "success"){
                    Intent intent = new Intent(CaptchaConfirmActivity.this,PasswordChangeActivity.class);
                    intent.putExtra("phone",phoneInputText.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }
}