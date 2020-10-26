package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.AlarmListItem;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.alarm.AlarmPlay;

import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.games.BlowingGameActivity;
import com.example.wowtime.ui.games.ShakingGameActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AlarmItemAdapter extends BaseAdapter {
    private List<AlarmListItem> mData;
    private Context mContext;
    private AlarmManager alarmManager;
    private PendingIntent pi;

    public AlarmItemAdapter(List<AlarmListItem> mData, Context mContext) {

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

    @SuppressLint({"ViewHolder", "SetTextI18n","DefaultLocale"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item, parent, false);
        TextView txt_tag = (TextView) convertView.findViewById(R.id.AlarmTag);
        TextView txt_time=(TextView) convertView.findViewById(R.id.AlarmTime);
        txt_tag.setText(mData.get(position).getTag());
        String hour=String.format("%02d",mData.get(position).getHour());
        String minute=String.format("%02d",mData.get(position).getMinute());
        txt_time.setText(hour+":"+minute);

        CardView cardView=(CardView)convertView.findViewById(R.id.AlarmCard);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClockSettingActivity.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
       }
        );

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("long click");
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                dialog.setTitle("删除闹钟");//设置标题
//                dialog.setMessage("something important");//设置信息具体内容
                dialog.setCancelable(true);//设置是否可取消
                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override//设置ok的事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("ok");
                        mData.remove(position);
                        SharedPreferences mySharedPreferences= mContext.getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
                        String shared= JSONObject.toJSONString(mData);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("list",shared);
                        editor.apply();
                        notifyDataSetInvalidated();
                        //在此处写入ok的逻辑
                    }
                });
                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch trigger=convertView.findViewById(R.id.AlarmSwitch);
        trigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,mData.get(position).getHour());
                    calendar.set(Calendar.MINUTE,mData.get(position).getMinute());
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);
                    Calendar currentTime=Calendar.getInstance();
                    alarmManager= (AlarmManager) (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent;
                    if(mData.get(position).getGame().equals("努力吹吹吹")){
                        intent= new Intent(mContext, BlowingGameActivity.class);
                    }
                    else if(mData.get(position).getGame().equals("使劲摇摇摇")){
                        intent= new Intent(mContext, ShakingGameActivity.class);
                    }
                    else {
                        intent=new Intent(mContext,AlarmPlay.class);
                    }
                    intent.putExtra("ring",mData.get(position).getRing());
                    pi=PendingIntent.getActivity(mContext, 0, intent, 0);
                    if(mData.get(position).getFrequency().equals("无重复")) {
                        System.out.println("无重复闹钟");
                        if (calendar.getTimeInMillis()<=currentTime.getTimeInMillis()){
                            calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
                        }
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                    }
                    else if(mData.get(position).getFrequency().equals("每天")){
                        System.out.println("每日闹钟");
                        if (calendar.getTimeInMillis()<=currentTime.getTimeInMillis()){
                            calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
                        }
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),24*60*60*1000, pi);
                    }
                    else{
                        int currentWeekday=calendar.get(Calendar.DAY_OF_WEEK);
                        int setWeekday=currentWeekday;
                        System.out.println(mData.get(position).getFrequency()+"闹钟");
                        System.out.println("currentWeekday:"+currentWeekday);
                        if(mData.get(position).getFrequency().equals("每周一")){
                            setWeekday=2;
                        }
                        if(mData.get(position).getFrequency().equals("每周二")){
                            setWeekday=3;
                        }
                        if(mData.get(position).getFrequency().equals("每周三")){
                            setWeekday=4;
                        }
                        if(mData.get(position).getFrequency().equals("每周四")){
                            setWeekday=5;
                        }
                        if(mData.get(position).getFrequency().equals("每周五")){
                            setWeekday=6;
                        }
                        if(mData.get(position).getFrequency().equals("每周六")){
                            setWeekday=7;
                        }
                        if(mData.get(position).getFrequency().equals("每周日")){
                            setWeekday=1;
                        }
                        int delta_day=setWeekday-currentWeekday;
                        if(delta_day<0){
                            delta_day+=7;
                        }
                        long start_time=calendar.getTimeInMillis()+24*60*60*1000*delta_day;
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,start_time,24*60*60*1000*7, pi);
                    }
                }else{
                    alarmManager.cancel(pi);
                }
            }
        });
        return convertView;
    }
}
