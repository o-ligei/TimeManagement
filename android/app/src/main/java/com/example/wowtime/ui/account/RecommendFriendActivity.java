package com.example.wowtime.ui.account;

import android.os.Handler;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.wowtime.util.Ajax;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.adapter.InternetFriendItemAdapter;
import com.example.wowtime.dto.InternetFriendItem;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.util.ArrayList;
import java.util.Collections;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;


public class RecommendFriendActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<InternetFriendItem> list = new ArrayList<>();
    InternetFriendItemAdapter friendsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_friend);
        listView = findViewById(R.id.friend_internet_list_r);
        friendsListAdapter = new InternetFriendItemAdapter(list, getApplicationContext());
        listView.setAdapter(friendsListAdapter);
        GetRecommendFriend();
    }

    private void GetRecommendFriend() {
        FormBody.Builder formBody = new Builder();
        formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));

        Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
                String str_data = message.getData().get("data").toString();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(str_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list.clear();
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
                friendsListAdapter.notifyDataSetChanged();
            }
            return false;
        });

        Ajax ajax = new Ajax("/Social/RecommendFriend", formBody, handler, InternetConstant.FETCH);
        ajax.fetch();
    }
}