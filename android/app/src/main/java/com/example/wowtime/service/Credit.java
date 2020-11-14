package com.example.wowtime.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.wowtime.R;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import org.json.JSONException;
import com.alibaba.fastjson.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.FormBody;

public class Credit {
    public Credit(){}

    public void modifyCredit(int n,String eventName) {
        /*timestamp*/
        System.out.println("modify credit");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("timestamp"+sdf.format(date));

        /*formBody*/
        System.out.println(eventName+" "+n);
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("userid", UserInfoAfterLogin.userid.toString());
        formBody.add("event", eventName+" "+n);
        formBody.add("timestamp",sdf.format(date));
        formBody.add("earn", String.valueOf(n));

        /*handler*/
        android.os.Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
                String msg= message.getData().get("msg").toString();
                String data=message.getData().get("data").toString();
                if(msg.equals("success")){
                    System.out.println("save detail success");
                }
                else{
                    System.out.println("save detail failed");
                }
//                String result = (String) msg.obj;
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String message = null;
//                try {
//                    message = jsonObject.get("msg").toString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                assert message != null;
//                if(message.equals("success")){
//                    System.out.println("save detail success");
//                }
//                else{
//                    System.out.println("save detail failed");
//                }
            }
            return false;
        });

        Ajax ajax=new Ajax("/Detail/SaveDetail",formBody,handler,InternetConstant.FETCH);
        ajax.fetch();
    }

}
