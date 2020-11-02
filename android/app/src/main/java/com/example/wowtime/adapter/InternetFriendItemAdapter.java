package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.dto.FriendsListItem;
import com.example.wowtime.dto.InternetFriendItem;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternetFriendItemAdapter extends BaseAdapter {


    private ArrayList<InternetFriendItem> mData;
    private Context mContext;

    public InternetFriendItemAdapter(ArrayList<InternetFriendItem> mData, Context mContext) {

        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.internet_friend_item,parent,false);

        ImageView img_icon = (ImageView) convertView.findViewById(R.id.user_icon_internet);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.username_internet);
        Button add_friend = convertView.findViewById(R.id.add_friend_button);
        add_friend.setOnClickListener(v->OKAddFriend(mData.get(position).getUserId()));

        String userIcon = mData.get(position).getUserIcon();
        byte[] decodedString = Base64.decode(userIcon, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0 , decodedString.length);

        img_icon.setImageBitmap(decodedByte);
        txt_aName.setText(mData.get(position).getUsername());
        return convertView;
    }

    private Handler handler = new Handler(msg -> {
        if (msg.what == 1) {
            String response = (String) msg.obj;
            Toast toast = Toast.makeText(mContext,response,Toast.LENGTH_SHORT);
            toast.show();
        }
        return false;
    });

    private void OKAddFriend(Integer to){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                System.out.println(to);
                formBody.add("from", String.valueOf(UserInfoAfterLogin.userid));
                formBody.add("to", String.valueOf(to));
                System.out.println("from"+ UserInfoAfterLogin.userid +"to"+ to +"/Social/AddFriend");
                Ajax ajax = new Ajax("/Social/AddFriend",formBody,handler,1);
                ajax.fetch();
            }
        }).start();
    }

}