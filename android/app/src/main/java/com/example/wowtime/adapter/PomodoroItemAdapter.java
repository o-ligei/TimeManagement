package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.PomodoroListItem;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.pomodoro.PomodoroSettingActivity;

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

        CardView cardView=convertView.findViewById(R.id.PomodoroCard);
        cardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("pomodoroListPosition:"+position);
                        Intent intent = new Intent(mContext, PomodoroSettingActivity.class);
                        intent.putExtra("position", position);
                        mContext.startActivity(intent);
                    }
                }
        );
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("long click");
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                dialog.setTitle(mContext.getResources().getText(R.string.pomodoro_delete_title));//设置标题
//                dialog.setMessage("something important");//设置信息具体内容
                dialog.setCancelable(true);//设置是否可取消
                dialog.setPositiveButton(mContext.getResources().getText(R.string.pomodoro_delete_confirm), new DialogInterface.OnClickListener() {
                    @Override//设置ok的事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mData.remove(position);
                        SharedPreferences mySharedPreferences= mContext.getSharedPreferences("pomodoroList", Activity.MODE_PRIVATE);
//                        SharedPreferences mySharedPreferences=PomodoroSettingActivity.getPomodoroSp();
                        String shared= JSONObject.toJSONString(mData);
                        System.out.println("listBeforeRemoving:"+mySharedPreferences.getString("pomodoroList",""));
                        System.out.println("listAfterRemoving"+shared);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("pomodoroList",shared);
                        editor.commit();
                        Toast.makeText(mContext,mContext.getResources().getText(R.string.pomodoro_save_successfully),Toast.LENGTH_LONG).show();
                        notifyDataSetInvalidated();
                        //在此处写入ok的逻辑
                    }
                });
                dialog.setNegativeButton(mContext.getResources().getText(R.string.pomodoro_delete_cancel), new DialogInterface.OnClickListener() {
                    @Override//设置取消事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("cancel");
                        //在此写入取消的事件
                    }
                });
                dialog.show();
                return true;
            }
        });
        return convertView;
    }
}
