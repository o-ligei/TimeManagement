package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.wowtime.R;
import com.example.wowtime.dto.OptionGameItem;

import java.util.ArrayList;

public class OptionGameAdapter extends BaseAdapter {

    private ArrayList<OptionGameItem> mData;
    private Context mContext;
    private Integer green = -1;
    private String choose = "";

    public OptionGameAdapter(ArrayList<OptionGameItem> mData, Context mContext) {
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

    public void setGreen(Integer green) {this.green = green;}
    public String getChoose() {return choose;}
    public void setChoose(int position) {
        OptionGameItem optionGameItem = (OptionGameItem) getItem(position);
        choose = optionGameItem.getOpt();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.option_item, parent,false);
        TextView optionView = convertView.findViewById(R.id.option_item_option);
        TextView statementView = convertView.findViewById(R.id.option_item_statement);
        CardView cardView = convertView.findViewById(R.id.option_item_card);
        optionView.setText(mData.get(position).getOpt());
        statementView.setText(mData.get(position).getStatement());
        if (position == green) cardView.setBackgroundColor(mContext.getResources().getColor(R.color.lightPink));
        return convertView;
    }
}
