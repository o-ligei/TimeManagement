package com.example.wowtime.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.*;

import com.example.wowtime.R;
import com.example.wowtime.service.Credit;
import com.example.wowtime.ui.others.CreditDetailListActivity;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonInfoFragment extends Fragment {
    TextView usernameText;
    TextView passwordText;
    TextView emailText;
    TextView genderText;
    TextView creditText;

    ConstraintLayout passwordLayout;
    ConstraintLayout emailLayout;
    ConstraintLayout creditLayout;
    public PersonInfoFragment(){}

    public PersonInfoFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetchCredit(View view){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("userid", UserInfoAfterLogin.userid.toString());

        android.os.Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
                String msg= message.getData().get("msg").toString();
                String data=message.getData().get("data").toString();
                System.out.println("msg"+msg);
                System.out.println("data"+data);
                org.json.JSONObject jsonObject = null;
                if(msg.equals("success")){
                    try {
                        jsonObject=new org.json.JSONObject(data);
                        data=jsonObject.get("credit").toString();
                        jsonObject=new org.json.JSONObject(data);
                        data=jsonObject.get("score").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TextView credit_detail=view.findViewById(R.id.Credit);
                    credit_detail.setText(data);
                }
                else{
                    System.out.println("failed");
                }

//                String result = (String) msg.obj;
//                org.json.JSONObject jsonObject = null;
//                String message = null;
//                String data=null;
//                try {
//                    jsonObject = new org.json.JSONObject(result);
//                    message = jsonObject.get("msg").toString();
//                    data=jsonObject.get("data").toString();
//                } catch (org.json.JSONException e) {
//                    e.printStackTrace();
//                }
//                assert message != null;
//                if(message.equals("success")){
//                    try {
//                        jsonObject=new org.json.JSONObject(data);
//                        data=jsonObject.get("credit").toString();
//                        jsonObject=new org.json.JSONObject(data);
//                        data=jsonObject.get("score").toString();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    TextView credit_detail=view.findViewById(R.id.CreditdetailLayout);
//                    credit_detail.setText(data);
//                }
//                else{
//                    System.out.println("failed");
//                }
            }
            return false;
        });

        Ajax ajax=new Ajax("/User/GetPersonalCredit",formBody,handler,InternetConstant.FETCH);
        ajax.fetch();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_activity, container, false);
        passwordLayout = root.findViewById(R.id.PasswordLayout);
        passwordLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class).putExtra("target","password")));
        emailLayout = root.findViewById(R.id.EmailLayout);
        emailLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class).putExtra("target","email")));
        creditLayout = root.findViewById(R.id.CreditLayout);
        creditLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreditDetailListActivity.class)));
        usernameText = root.findViewById(R.id.Username);
        passwordText = root.findViewById(R.id.Password);
        emailText = root.findViewById(R.id.Email);
        genderText = root.findViewById(R.id.Gender);
        creditText = root.findViewById(R.id.Credit);
        OKGetProfile();
        fetchCredit(root);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        OKGetProfile();
    }

    private void OKGetProfile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("userid",String.valueOf(UserInfoAfterLogin.userid));
                Request request = new Request.Builder().url(InternetConstant.host + "/User/GetPersonalProfile").post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    GetProfile(result);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
  
    private void GetProfile(String result ) throws JSONException{
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                String msg = null;
                String str_data = null;
                String username = null;
                String userIcon = null;
                String phone = null;
                String email = null;
                try {
                    jsonObject = new JSONObject(result);
                    msg = jsonObject.get("msg").toString();
                    str_data = jsonObject.get("data").toString();
                    JSONObject data = new JSONObject(str_data);
                    username = data.get("username").toString();
                    userIcon = data.get("userIcon").toString();
                    phone = data.get("phone").toString();
                    email = data.get("email").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
//                toast.show();
                if(msg.equals("success"))
                {
                    usernameText.setText(username);
                    passwordText.setText("*******");
                    emailText.setText(email);
                }
                if(!emailText.getText().toString().equals("null"))
                    emailLayout.setEnabled(false);
            }
        });
    }
}