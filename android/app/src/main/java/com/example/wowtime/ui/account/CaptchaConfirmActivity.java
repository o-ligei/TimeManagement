package com.example.wowtime.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.util.Ajax;
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

    private Handler handler = new Handler(msg -> {
        if (msg.what == 1) {
            String response = (String) msg.obj;
            Toast toast = Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT);
            toast.show();
        }
        return false;
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha_confirm);

        phoneInputText = findViewById(R.id.phone_input_in_cap);
        captchaConfirmText = findViewById(R.id.cap_input_in_cap);

        Button btn_getCaptcha = findViewById(R.id.get_cap_in_cap);
        btn_getCaptcha.setOnClickListener(v -> OKGetCaptcha());

        btn_handson = findViewById(R.id.handson_in_cap);
//        btn_handson.setEnabled(false);
        btn_handson.setOnClickListener(v -> OKGoToNext());
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
                    Response response = client.newCall(request).execute();
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

    void OKGoToNext(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("phone",phoneInputText.getText().toString());
                formBody.add("captcha",captchaConfirmText.getText().toString());
//                Ajax ajax = new Ajax("/User/SendCaptchaToPhone",formBody,handler,1);
//                ajax.fetch();
                Request request = new Request.Builder().url(InternetConstant.host + "/User/SendCaptchaToPhone").post(formBody.build()).build(); //TODO:change the url
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    GoToNext(result);
                } catch (IOException | JSONException e) {
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
                    String target = null;
                    target = getIntent().getStringExtra("target");
                    System.out.println(target);
                    if(target.equals("email"))
                        intent = new Intent(CaptchaConfirmActivity.this,EmailActivateActivity.class);
                    else if(target.equals("password"))
                        intent = new Intent(CaptchaConfirmActivity.this,PasswordChangeActivity.class);
                    intent.putExtra("phone",phoneInputText.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}