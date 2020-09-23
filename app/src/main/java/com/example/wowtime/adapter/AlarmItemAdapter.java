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

import java.util.ArrayList;

public class AlarmItemAdapter extends BaseAdapter {
    private ArrayList<AlarmListItem> mData;
    private Context mContext;

    public AlarmItemAdapter(ArrayList<AlarmListItem> mData, Context mContext) {

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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_alarm_list_item,parent,false);

        TextView txt_tag = (TextView) convertView.findViewById(R.id.AlarmTag);
        TextView txt_time=(TextView) convertView.findViewById(R.id.AlarmTime);
        txt_tag.setText(mData.get(position).getTag());
        txt_time.setText(mData.get(position).getTime());

        return convertView;
    }
}
