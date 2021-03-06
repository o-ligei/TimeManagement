package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.sip.SipSession;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wowtime.R;
import com.example.wowtime.dto.FriendsListItem;
import com.example.wowtime.ui.account.LoginActivityWithAuthActivity;
import com.example.wowtime.ui.account.LoginActivityWithPasswordActivity;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.pomodoro.PomodoroSettingActivity;

import java.util.ArrayList;

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

        convertView = LayoutInflater.from(mContext)
                                    .inflate(R.layout.friends_list_item, parent, false);

        ImageView friendIcon = convertView.findViewById(R.id.UserIcon);
        TextView username = convertView.findViewById(R.id.Username);
        Button btn_set_alarm = convertView.findViewById(R.id.btn_set_alarm_for_friend);
        btn_set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClockSettingActivity.class);
                intent.putExtra("userid", mData.get(position).getUserId());
                intent.putExtra("action", "set for friend");
                mContext.startActivity(intent);
            }
        });
//        ImageView cancelIcon = convertView.findViewById(R.id.CancelIcon);

        Integer userId = mData.get(position).getUserId();
        String userIcon = mData.get(position).getUserIcon();

        byte[] decodedString = Base64.decode(userIcon, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        friendIcon.setImageBitmap(decodedByte);
        username.setText(mData.get(position).getUsername());
//        cancelIcon.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
        return convertView;
    }
}
