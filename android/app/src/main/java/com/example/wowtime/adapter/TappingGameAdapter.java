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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TappingGameAdapter extends BaseAdapter {
    private ArrayList<String> mData;
    private Context mContext;
    private Set<Integer> green = new HashSet<>();
    private Set<Integer> red = new HashSet<>();

    public TappingGameAdapter(ArrayList<String> mData, Context mContext) {
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

    public void setSelection(Set<Integer> green, Set<Integer> red) {
        this.green = green;
        this.red = red;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.tapping_game_item, parent,false);
        CardView cardView = convertView.findViewById(R.id.tappingCard);
        if (green.contains(position)) cardView.setCardBackgroundColor(Color.GREEN);
        if (red.contains(position)) cardView.setCardBackgroundColor(Color.RED);
        return convertView;
    }
}
