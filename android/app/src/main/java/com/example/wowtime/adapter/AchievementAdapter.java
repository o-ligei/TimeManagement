package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.others.AchivementDetailActivity;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AchievementAdapter extends BaseAdapter {

    private ArrayList<String> mData;
    ArrayList<String> mContent;
    private Context mContext;

    public AchievementAdapter(ArrayList<String> mData, ArrayList<String> mContent,
            Context mContext) {

        this.mData = mData;
        this.mContext = mContext;
        this.mContent = mContent;
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
                                    .inflate(R.layout.achievement_item, parent, false);

        TextView txt_aName = (TextView) convertView.findViewById(R.id.achievementTitle);

        txt_aName.setText(mData.get(position));

        ImageView imageView = convertView.findViewById(R.id.achievementIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AchivementDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("title", mData.get(position));
                intent.putExtra("content", mContent.get(position));
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
