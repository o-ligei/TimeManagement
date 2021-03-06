package com.example.wowtime.ui.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.wowtime.R;
import com.example.wowtime.adapter.FriendsListAdapter;
import com.example.wowtime.dto.FriendsListItem;
import com.example.wowtime.ui.account.InternetFriendRequestActivity;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsListFragment extends Fragment {

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

    private class FriendRequestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            FlushFriendRequest();
        }
    }

    private FriendRequestReceiver friendRequestReceiver;
    private IntentFilter intentFilter;

    private void doRegisterReceiver() {
        friendRequestReceiver = new FriendRequestReceiver();
        intentFilter = new IntentFilter("friend request");
        requireActivity().registerReceiver(friendRequestReceiver, intentFilter);
    }

    private void FlushFriendRequest() {
        System.out.println("flush friend request");
        if (UserInfoAfterLogin.webSocketMessage) {
            friendRequest.setVisibility(View.VISIBLE);
        } else {
            friendRequest.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friend_list_fragment, container, false);

        listView = (ListView) root.findViewById(R.id.friends_list);
        searchText = (SearchView) root.findViewById(R.id.searchText_local);

        friendsListAdapter = new FriendsListAdapter(allfriendsListItems, getContext());
        listView.setAdapter(friendsListAdapter);

        friendRequest = root.findViewById(R.id.friend_notice);

        friendRequest.setOnClickListener(
                v -> startActivity(new Intent(getActivity(), InternetFriendRequestActivity.class)));

        searchText.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println("onQueryTextSubmit:" + s);
                if (s.isEmpty()) {
//                    System.out.println("empty!");
                    FriendsListAdapter friendsListAdapter = new FriendsListAdapter(
                            allfriendsListItems, getContext());
                    listView.setAdapter(friendsListAdapter);
                    return false;
                }
                searchfriendsListItems.clear();
                for (FriendsListItem item : allfriendsListItems) {
                    if (item.getUsername().contains(s)) {
                        searchfriendsListItems.add(item);
                    }
                }
                FriendsListAdapter friendsListAdapter = new FriendsListAdapter(
                        searchfriendsListItems, getContext());
                listView.setAdapter(friendsListAdapter);
//                System.out.println("flush");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                System.out.println("onQueryTextChange:" + s);
                return onQueryTextSubmit(s);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resume!");
        OKGetFriends();
        doRegisterReceiver();
        FlushFriendRequest();
    }

    private void OKGetFriends() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));
                Request request = new Request.Builder()
                        .url(InternetConstant.host + "/Social/GetFriendsList")
                        .post(formBody.build()).build();
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

    private void GetFriends(String result) throws JSONException {
        System.out.println(getActivity());
        getActivity().runOnUiThread(new Runnable() {
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
                allfriendsListItems.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(i + " item");
                    JSONObject item = null;
                    FriendsListItem listItem = null;
                    try {
                        item = (JSONObject) jsonArray.get(i);
                        listItem = new FriendsListItem(
                                Integer.valueOf(item.get("userId").toString()),
                                String.valueOf(item.get("userIcon")),
                                String.valueOf(item.get("username")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    allfriendsListItems.add(listItem);
                }
                friendsListAdapter.notifyDataSetChanged();
                //achievement needs to know the number of friends
                Integer number = allfriendsListItems.size();
                assert getActivity() != null;
                SharedPreferences achievement = getActivity()
                        .getSharedPreferences("achievement", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = achievement.edit();
                editor.putString("friend_have", number.toString());
                editor.apply();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(friendRequestReceiver);
    }
}