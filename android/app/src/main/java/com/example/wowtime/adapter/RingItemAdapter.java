package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wowtime.R;

import java.util.ArrayList;

public class RingItemAdapter extends BaseAdapter {
    private ArrayList<String> mData;
    private Context mContext;

    public RingItemAdapter(ArrayList<String> mData, Context mContext) {

        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.ring_list_item,parent,false);

        TextView txt_aName = (TextView) convertView.findViewById(R.id.RingName);

        txt_aName.setText(mData.get(position));

        return convertView;
    }
}
