package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.component.AppListItem;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameItemAdapter extends BaseAdapter {
    private ArrayList<String> mData;
    private Context mContext;

    public GameItemAdapter(ArrayList<String> mData, Context mContext) {

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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_game_list_item,parent,false);

        TextView txt_aName = (TextView) convertView.findViewById(R.id.GameName);

        txt_aName.setText(mData.get(position));

        return convertView;
    }
}
