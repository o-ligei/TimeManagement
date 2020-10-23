package com.example.wowtime.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordChangeActivity extends AppCompatActivity {

    TextView passwordInputView;
    TextView passwordConfirmView;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        passwordInputView.findViewById(R.id.pass_change_in_pas_cha);
        passwordConfirmView.findViewById(R.id.pass_confirm_in_pass_cha);

        Button handson_btn = findViewById(R.id.handson_in_pass_cha);
        handson_btn.setOnClickListener(v -> OKHandsOn());

        phone = getIntent().getStringExtra("phone");
    }

    void OKHandsOn(){
        if(!passwordInputView.getText().toString().equals(passwordConfirmView.getText().toString())){
            Toast.makeText(getApplicationContext(),"密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("phone",phone);
                formBody.add("password",passwordInputView.getText().toString());
                Request request = new Request.Builder().url(InternetConstant.host + "/User/SendCaptchaToPhone").post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    handsOn(result);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void handsOn(String result) throws JSONException{
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
                String userid = null;
                try {
                    msg = jsonObject.get("msg").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                toast.show();
                if(msg.equals("success"))
                {
                    UserInfoAfterLogin.userid = Integer.valueOf(userid);
                    Intent intent = new Intent(PasswordChangeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}