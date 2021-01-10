package com.example.wowtime.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class Ajax {

    private String url;
    private FormBody.Builder formBody;
    private Handler mHandler;
    private int mWhat;

    public Ajax() {}

    public Ajax(String requestUrl, FormBody.Builder data, Handler handler, int what) {
        url = InternetConstant.host + requestUrl;
        formBody = data;
        mHandler = handler;
        mWhat = what;
    }

    private void checkMsg(String result) {
        System.out.println("ajax: result " + result);
        Bundle bundle = new Bundle();
        Message message = Message.obtain();
        JSONObject jsonObject = null;
        String msg = null;
        String data = null;
        try {
            jsonObject = new JSONObject(result);
            msg = jsonObject.get("msg").toString();
            data = jsonObject.get("data").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle.putString("msg", msg);
        bundle.putString("data", data);
        message.setData(bundle);
        message.what = mWhat;
//        message.obj = result;
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    public void fetch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    checkMsg(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
