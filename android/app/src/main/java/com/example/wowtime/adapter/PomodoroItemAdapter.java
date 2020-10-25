package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.dto.PomodoroListItem;

import java.util.ArrayList;
import java.util.List;

public class PomodoroItemAdapter extends BaseAdapter {
    private List<PomodoroListItem> mData;
    private Context mContext;
    private OnItemClickListener onItemClickListener = null;

    public PomodoroItemAdapter(List<PomodoroListItem> mData, Context mContext) {

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

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener=onItemClickListener;
        System.out.println("set pomodoro click listener successfully");
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.pomodoro_list_item,parent,false);

        TextView txt_name = (TextView) convertView.findViewById(R.id.PomodoroName);
        TextView txt_other=(TextView) convertView.findViewById(R.id.PomodoroOther);
        TextView txt_gap = (TextView) convertView.findViewById(R.id.PomodoroTotalGap);

        PomodoroListItem pomodoroListItem=mData.get(position);
        String mode;
        switch (pomodoroListItem.getMode()){
            case 0:
                mode="强模式";
                break;
            case 1:
                mode="弱模式";
                break;
            default:
                mode="";
        }
        int totalGap=pomodoroListItem.getTotalGap();
        int hour=totalGap/60;
        if(totalGap<60)hour=0;
        int minute=totalGap-hour*60;
        int workGap=pomodoroListItem.getWorkGap();
        int restGap=pomodoroListItem.getRestGap();

        txt_name.setText(pomodoroListItem.getName());
        txt_other.setText(mode+" 每专注"+workGap+"min 可休息"+restGap+"min");
        txt_gap.setText((hour==0?"":(hour+"hour"))+minute+"min");

        return convertView;
    }
}
