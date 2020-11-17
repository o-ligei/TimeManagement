package com.example.wowtime.ui.others;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.FriendsListAdapter;
import com.example.wowtime.dto.FriendsListItem;
import com.example.wowtime.ui.account.InternetFriendRequestActivity;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.widget.SearchView.OnQueryTextListener;

public class FriendsListFragment extends Fragment {

//    private String initImage = "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";

    ListView listView;
    ArrayList<FriendsListItem> allfriendsListItems = new ArrayList<>();
    ArrayList<FriendsListItem> searchfriendsListItems = new ArrayList<>();
    SearchView searchText;
    FriendsListAdapter friendsListAdapter;
    TextView friendRequest;

    public FriendsListFragment() {
    }

    public FriendsListFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    private class FriendRequestReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    }
    private FriendRequestReceiver friendRequestReceiver;
    private IntentFilter intentFilter;

//    private void doRegisterReceiver() {
//        friendRequestReceiver = new FriendRequestReceiver();
//        intentFilter = new IntentFilter("friend request");
//        registerReceiver(friendRequestReceiver, intentFilter);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friend_list_fragment, container, false);
        OKGetFriends();
        listView = (ListView) root.findViewById(R.id.friends_list);
        searchText = (SearchView) root.findViewById(R.id.searchText_local);

        friendsListAdapter = new FriendsListAdapter(allfriendsListItems, getContext());
        listView.setAdapter(friendsListAdapter);

        friendRequest = root.findViewById(R.id.friend_notice);

        friendRequest.setOnClickListener(v -> startActivity(new Intent(getActivity(), InternetFriendRequestActivity.class)));

        searchText.setOnQueryTextListener(new OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println("onQueryTextSubmit:"+s);
                if(s.isEmpty()){
                    System.out.println("empty!");
                    FriendsListAdapter friendsListAdapter = new FriendsListAdapter(allfriendsListItems, getContext());
                    listView.setAdapter(friendsListAdapter);
                    return false;
                }
                searchfriendsListItems.clear();
                for(FriendsListItem item : allfriendsListItems){
                    if(item.getUsername().contains(s))
                    {
                        searchfriendsListItems.add(item);
                    }
                }
                FriendsListAdapter friendsListAdapter = new FriendsListAdapter(searchfriendsListItems, getContext());
                listView.setAdapter(friendsListAdapter);
                System.out.println("flush");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                System.out.println("onQueryTextChange:"+s);
                return onQueryTextSubmit(s);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(UserInfoAfterLogin.webSocketMessage){
            friendRequest.setVisibility(View.VISIBLE);
        }else{
            friendRequest.setVisibility(View.GONE);
        }
    }

    private void OKGetFriends(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));
                Request request = new Request.Builder().url(InternetConstant.host + "/Social/GetFriendsList").post(formBody.build()).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    GetFriends(result);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void GetFriends(String result) throws JSONException{
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("result:"+result);
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
                allfriendsListItems.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    System.out.println(i+" item");
                    JSONObject item = null;
                    FriendsListItem listItem = null;
                    try {
                        item = (JSONObject) jsonArray.get(i);
                        listItem = new FriendsListItem(Integer.valueOf(item.get("userId").toString()),String.valueOf(item.get("userIcon")),String.valueOf(item.get("username")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    allfriendsListItems.add(listItem);
                }
                friendsListAdapter.notifyDataSetChanged();
            }
        });
    }
}