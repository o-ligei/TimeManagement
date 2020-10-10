package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.component.AlarmListItem;
import com.example.wowtime.component.PomodoroListItem;

import java.util.ArrayList;

public class PomodoroItemAdapter extends BaseAdapter {
    private ArrayList<PomodoroListItem> mData;
    private Context mContext;

    public PomodoroItemAdapter(ArrayList<PomodoroListItem> mData, Context mContext) {

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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.pomodoro_list_item,parent,false);

        TextView txt_name = (TextView) convertView.findViewById(R.id.PomodoroName);
        TextView txt_gap = (TextView) convertView.findViewById(R.id.PomodoroGap);
        txt_name.setText(mData.get(position).getName());
        txt_gap.setText(mData.get(position).getGap());

        return convertView;
    }
}
