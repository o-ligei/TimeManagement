package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.component.AppListItem;
import com.example.wowtime.component.FriendsListItem;

import java.util.ArrayList;
import java.util.LinkedList;

public class FriendsListAdapter extends BaseAdapter {

    private ArrayList<FriendsListItem> mData;
    private Context mContext;

    public FriendsListAdapter(ArrayList<FriendsListItem> mData, Context mContext) {
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.friends_list_item,parent,false);

        ImageView friendIcon = convertView.findViewById(R.id.FriendIcon);
        TextView username = convertView.findViewById(R.id.Username);
        ImageView cancelIcon = convertView.findViewById(R.id.CancelIcon);

        Integer userId = mData.get(position).getUserId();
        String userIcon = mData.get(position).getUserIcon();
        System.out.println(userIcon);
        byte[] decodedString = Base64.decode(userIcon, Base64.DEFAULT);
        System.out.println("We are here"+userId);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0 , decodedString.length);
        friendIcon.setImageBitmap(decodedByte);
        username.setText(mData.get(position).getUsername());
        cancelIcon.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
        return convertView;
    }
}
