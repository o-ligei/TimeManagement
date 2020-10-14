package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.dto.CreditDetailListItem;

import java.util.ArrayList;

public class CreditDetailListAdapter extends BaseAdapter {

    private ArrayList<CreditDetailListItem> mData;
    private Context mContext;

    public CreditDetailListAdapter(ArrayList<CreditDetailListItem> mData, Context mContext) {
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.credit_detail_list_item,parent,false);

        TextView timestamp = convertView.findViewById(R.id.Timestamp);
        TextView eventName = convertView.findViewById(R.id.Eventname);
        TextView variation = convertView.findViewById(R.id.Variation);

        String timestampStr = mData.get(position).getTimestamp().toString();
        String eventNameStr = mData.get(position).getEventName();
        String variationStr = mData.get(position).getVariation().toString();

        timestamp.setText(timestampStr);
        eventName.setText(eventNameStr);
        variation.setText(variationStr);
        return convertView;
    }
}
