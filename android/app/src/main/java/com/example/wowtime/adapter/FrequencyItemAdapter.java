package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import java.util.ArrayList;

public class FrequencyItemAdapter extends BaseAdapter {

    private ArrayList<String> mData;
    private Context mContext;
    private ArrayList<Boolean> options;
    private int flag;

    public FrequencyItemAdapter(ArrayList<String> mData, Context mContext) {

        this.mData = mData;
        this.mContext = mContext;
        options = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            options.add(false);
        }
        flag = 0;
    }

    private void checkFlag() {
        if (options.get(0)) {
            flag = 1;
            return;
        }
        for (int i = 1; i < options.size(); i++) {
            if (options.get(i)) {
                flag = -1;
                return;
            }
        }
        flag = 0;
    }

    public void sendOptions() {
        SharedPreferences mySharedPreferences = mContext
                .getSharedPreferences("clock", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        String shared = JSONObject.toJSONString(options);
        System.out.println("shared:" + shared);
        editor.putString("frequency", shared);
        editor.apply();
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
        convertView = LayoutInflater.from(mContext)
                                    .inflate(R.layout.frequency_check_box, parent, false);
        CheckedTextView txt_aName = (CheckedTextView) convertView
                .findViewById(R.id.FrequencySetting);
        txt_aName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean tmp = options.get(position);
                tmp = !tmp;
                options.set(position, tmp);
                notifyDataSetInvalidated();
            }
        });
        checkFlag();
        if (flag == -1) {
            if (position == 0) {
                txt_aName.setVisibility(View.GONE);
                return convertView;
            }
        } else if (flag == 1) {
            if (position != 0) {
                txt_aName.setVisibility(View.GONE);
                return convertView;
            }
        }
        txt_aName.setText(mData.get(position));
        txt_aName.setChecked(options.get(position));

        return convertView;
    }
}
