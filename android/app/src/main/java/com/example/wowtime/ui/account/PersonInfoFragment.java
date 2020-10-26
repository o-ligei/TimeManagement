package com.example.wowtime.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.others.CreditDetailListActivity;
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


    public PersonInfoFragment(){}

    public PersonInfoFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_activity, container, false);
        ConstraintLayout passwordLayout = root.findViewById(R.id.PasswordLayout);
        passwordLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class).putExtra("target","password")));
        ConstraintLayout emailLayout = root.findViewById(R.id.EmailLayout);
        emailLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class).putExtra("target","email")));
        ConstraintLayout creditLayout = root.findViewById(R.id.CreditLayout);
        creditLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreditDetailListActivity.class)));

        usernameText = root.findViewById(R.id.Username);
        passwordText = root.findViewById(R.id.Password);
        emailText = root.findViewById(R.id.Email);
        genderText = root.findViewById(R.id.Gender);
        creditText = root.findViewById(R.id.Credit);
        OKGetProfile();
        return root;
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
            }
        });
    }
}