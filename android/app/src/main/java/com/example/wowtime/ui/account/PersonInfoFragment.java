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

import okhttp3.FormBody;

public class PersonInfoFragment extends Fragment {
    public PersonInfoFragment(){}

    public PersonInfoFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    private void fetchCredit(View view){
//        FormBody.Builder formBody = new FormBody.Builder();
//        formBody.add("userid", UserInfoAfterLogin.userid.toString());
//
//        android.os.Handler handler = new Handler(message -> {
//            if (message.what == InternetConstant.FETCH) {
//                String msg= message.getData().get("msg").toString();
//                String data=message.getData().get("data").toString();
//                System.out.println("msg"+msg);
//                System.out.println("data"+data);
//                org.json.JSONObject jsonObject = null;
//                if(msg.equals("success")){
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
//            }
//            return false;
//        });
//
//        Ajax ajax=new Ajax("/User/GetPersonalCredit",formBody,handler,InternetConstant.FETCH);
//        ajax.fetch();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_activity, container, false);
        ConstraintLayout passwordLayout = root.findViewById(R.id.PasswordLayout);
        passwordLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class)));
        ConstraintLayout emailLayout = root.findViewById(R.id.EmailLayout);
        emailLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptchaConfirmActivity.class)));
//        ConstraintLayout creditLayout = root.findViewById(R.id.CreditLayout);
//        creditLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreditDetailListActivity.class)));
//        fetchCredit(root);
        return root;
    }
}