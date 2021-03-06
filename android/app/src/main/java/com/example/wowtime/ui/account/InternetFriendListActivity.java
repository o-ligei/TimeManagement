package com.example.wowtime.ui.account;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;
import com.example.wowtime.adapter.InternetFriendItemAdapter;
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

public class InternetFriendListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<InternetFriendItem> list = new ArrayList<>();
    SearchView searchView;
    InternetFriendItemAdapter friendsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_friend_list);

        listView = findViewById(R.id.friend_internet_list);
        searchView = findViewById(R.id.searchText_internet);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println("onQueryTextSubmit:" + s);
                OKGetInternetFriends(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                System.out.println("onQueryTextChange:" + s);
                return true;
            }
        });

        friendsListAdapter = new InternetFriendItemAdapter(list, getApplicationContext());
        listView.setAdapter(friendsListAdapter);
    }

    private void OKGetInternetFriends(String s) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("username", s);
        formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));

        Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
//                JSONObject jsonObject = null;
                String str_data = message.getData().get("data").toString();
                JSONArray jsonArray = null;
                try {
//                    jsonObject = new JSONObject(result);
//                    str_data = jsonObject.get("data").toString();
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
        Ajax ajax = new Ajax("/Social/GetProfile", formBody, handler, InternetConstant.FETCH);
        ajax.fetch();
    }


}