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

import java.util.LinkedList;

public class AppAdapter extends BaseAdapter {

    private LinkedList<AppListItem> mData;
    private Context mContext;

    public AppAdapter(LinkedList<AppListItem> mData, Context mContext) {

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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.white_list_item,parent,false);

        ImageView img_icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.name);
        TextView txt_aAlloc = (TextView) convertView.findViewById(R.id.alloc);

        img_icon.setBackgroundResource(mData.get(position).getIcon());
        txt_aName.setText(mData.get(position).getName());
        txt_aAlloc.setText(mData.get(position).getAllowed());

        return convertView;
    }
}
