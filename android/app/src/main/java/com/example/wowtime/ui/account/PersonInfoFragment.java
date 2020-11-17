package com.example.wowtime.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.wowtime.R;
import com.example.wowtime.service.Accumulation;
import com.example.wowtime.service.Credit;
import com.example.wowtime.ui.others.CreditDetailListActivity;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonInfoFragment extends Fragment {
    TextView usernameText;
//    TextView passwordText;
    TextView emailText;
//    TextView genderText;
    TextView creditText;
    TextView phoneNumberText;

    TextView forgetPassword;

    ImageView modifyEmail;

//    ConstraintLayout passwordLayout;
//    ConstraintLayout emailLayout;
//    ConstraintLayout creditLayout;
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
        forgetPassword = root.findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class).putExtra("target","password")));
        modifyEmail = root.findViewById(R.id.modifyEmail);
        modifyEmail.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class).putExtra("target","email")));
        creditText = root.findViewById(R.id.Credit);
//        System.out.println(creditText.getText());
        Accumulation accumulation=new Accumulation(requireContext());
        creditText.setText(String.valueOf(accumulation.getAccumulation()));
//        creditText.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreditDetailListActivity.class)));
        usernameText = root.findViewById(R.id.Username);
        emailText = root.findViewById(R.id.Email);
        phoneNumberText =root.findViewById(R.id.PhoneNumber);
//        OKGetProfile();
//        fetchCredit(root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        OKGetProfile();
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
//                    passwordText.setText("*******");
                    emailText.setText(email);
                    phoneNumberText.setText(phone);
                }
                if(!emailText.getText().toString().equals("null"))
                    emailText.setEnabled(false);
            }
        });
    }
}