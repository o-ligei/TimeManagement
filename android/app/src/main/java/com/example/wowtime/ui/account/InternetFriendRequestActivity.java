package com.example.wowtime.ui.account;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;
import com.example.wowtime.adapter.InternetFriendRequestItemAdapter;
import com.example.wowtime.dto.InternetFriendItem;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InternetFriendRequestActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<InternetFriendItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_friend_request);

        listView = findViewById(R.id.friend_internet_list_request);
        OKGetInternetFriends();
    }

    private void OKGetInternetFriends() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));

        Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
//                JSONObject jsonObject = null;
                String str_data = message.getData().get("data").toString();
                System.out.println("friend" + str_data);
                JSONArray jsonArray = null;
                try {
//                    jsonObject = new JSONObject(result);
//                    str_data = jsonObject.get("data").toString();
                    jsonArray = new JSONArray(str_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(i + " item");
                    JSONObject item = null;
                    InternetFriendItem listItem = null;
                    try {
                        item = (JSONObject) jsonArray.get(i);
                        listItem = new InternetFriendItem(
                                Integer.valueOf(item.get("userId").toString()),
                                String.valueOf(item.get("userIcon")),
                                String.valueOf(item.get("username")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.add(listItem);
                }
                InternetFriendRequestItemAdapter friendsListAdapter = new InternetFriendRequestItemAdapter(
                        list, getApplicationContext());
                listView.setAdapter(friendsListAdapter);
            }
            return false;
        });
        Ajax ajax = new Ajax("/Social/GetFriendRequest", formBody, handler, InternetConstant.FETCH);
        ajax.fetch();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//                formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));
//                Request request = new Request.Builder()
//                        .url(InternetConstant.host + "/Social/GetFriendRequest")
//                        .post(formBody.build()).build();
//                try {
//                    Response response = client.newCall(request).execute();//发送请求
//                    String result = response.body().string();
//                    GetInternetFriends(result);
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private void GetInternetFriends(String result) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("result:" + result);
                JSONObject jsonObject = null;
                String str_data = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(result);
                    str_data = jsonObject.get("data").toString();
                    jsonArray = new JSONArray(str_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(i + " item");
                    JSONObject item = null;
                    InternetFriendItem listItem = null;
                    try {
                        item = (JSONObject) jsonArray.get(i);
                        listItem = new InternetFriendItem(
                                Integer.valueOf(item.get("userId").toString()),
                                String.valueOf(item.get("userIcon")),
                                String.valueOf(item.get("username")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.add(listItem);
                }
                InternetFriendRequestItemAdapter friendsListAdapter = new InternetFriendRequestItemAdapter(
                        list, getApplicationContext());
                listView.setAdapter(friendsListAdapter);
            }
        });
    }
}