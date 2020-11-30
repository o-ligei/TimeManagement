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
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.games.BlowingGameActivity;
import com.example.wowtime.ui.games.CalculateGameActivity;
import com.example.wowtime.ui.games.OptionGameActivity;
import com.example.wowtime.ui.games.RandomNumberGameActivity;
import com.example.wowtime.ui.games.ShakingGameActivity;
import com.example.wowtime.ui.games.TappingGameActivity;
import com.example.wowtime.ui.pomodoro.FloatingImageDisplayService;
import com.example.wowtime.util.Weekday;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AlarmItemAdapter extends BaseAdapter {

    private List<AlarmListItem> mData;
    private Context mContext;
    private AlarmManager alarmManager;

    public AlarmItemAdapter(List<AlarmListItem> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    private void modifyState(boolean flag, int position) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences("alarmList",
                                                                              Activity.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mySharedPreferences
                .edit();
        String shared = mySharedPreferences.getString("list", "");
        List<AlarmListItem> alarmList = new ArrayList<>();
        if (!shared.equals("") && shared != null) {
            alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
        }
        AlarmListItem alarm = alarmList.get(position);
        alarm.setStatus(flag);
        alarmList.set(position, alarm);
        shared = JSONObject.toJSONString(alarmList);
        editor.putString("list", shared);
        editor.apply();
    }

    private Intent getIntent(int position) {
        Intent intent;
        if (mData.get(position).getGame().equals(mContext.getResources().getString(
                R.string.blowing_game_setting_header))) {
            intent = new Intent(mContext, BlowingGameActivity.class);
        } else if (mData.get(position).getGame().equals(mContext.getResources()
                                                                .getString(
                                                                        R.string.shaking_game_setting_header))) {
            intent = new Intent(mContext, ShakingGameActivity.class);
        } else if (mData.get(position).getGame().equals(mContext.getResources()
                                                                .getString(
                                                                        R.string.calculate_game_setting_header))) {
            intent = new Intent(mContext, CalculateGameActivity.class);
        } else if (mData.get(position).getGame().equals(mContext.getResources()
                                                                .getString(
                                                                        R.string.tapping_game_setting_header))) {
            intent = new Intent(mContext, TappingGameActivity.class);
        } else if (mData.get(position).getGame().equals(mContext.getResources()
                                                                .getString(
                                                                        R.string.Answering_game_setting_header))) {
            intent = new Intent(mContext, OptionGameActivity.class);
        } else {
            intent = new Intent(mContext, RandomNumberGameActivity.class);
        }
        intent.putExtra("ring", mData.get(position).getRing());
        intent.putExtra("sleepFlag", mData.get(position).getSleepFlag());
        return intent;
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

    @SuppressLint({"ViewHolder", "SetTextI18n", "DefaultLocale"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext)
                                    .inflate(R.layout.alarm_list_item, parent, false);
        TextView txt_tag = (TextView) convertView.findViewById(R.id.AlarmTag);
        TextView txt_time = (TextView) convertView.findViewById(R.id.AlarmTime);
        TextView txt_frequency = (TextView) convertView.findViewById(R.id.AlarmFrequency);
        List<Boolean> frequency = new ArrayList<>();
        frequency = mData.get(position).getFrequency();
        if (frequency.get(0)) {
            txt_frequency.setText(mContext.getResources().getString(R.string.alarm_frequency_no_repeat));
        } else {
            boolean flag=mContext.getResources()
                    .getString(R.string.alarm_frequency_no_repeat).equals("无重复");
            Weekday weekday = new Weekday(flag);
            StringBuilder out;
            if(flag) {
                out= new StringBuilder("星期");
            }
            else {
                out= new StringBuilder("");
            }
            for (int j = 1; j <= 7; j++) {
                if (frequency.get(j)) {
                    out.append(weekday.getDay(j));
                    out.append(" ");
                }
            }
            txt_frequency.setText(out);
        }
//        txt_frequency.setText(mData.get(position).getFrequency());
        txt_tag.setText(mData.get(position).getTag());
        String hour = String.format("%02d", mData.get(position).getHour());
        String minute = String.format("%02d", mData.get(position).getMinute());
        txt_time.setText(hour + ":" + minute);

        CardView cardView = (CardView) convertView.findViewById(R.id.AlarmCard);
        cardView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(mContext, ClockSettingActivity.class);
                                            intent.putExtra("position", position);
                                            mContext.startActivity(intent);
                                        }
                                    }
        );

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("long click");
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("删除闹钟");//设置标题
//                dialog.setMessage("something important");//设置信息具体内容
                dialog.setCancelable(true);//设置是否可取消
                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override//设置ok的事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("ok");
                        mData.remove(position);
                        SharedPreferences mySharedPreferences = mContext
                                .getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
                        String shared = JSONObject.toJSONString(mData);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mySharedPreferences
                                .edit();
                        editor.putString("list", shared);
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
        Switch trigger = convertView.findViewById(R.id.AlarmSwitch);
        trigger.setChecked(mData.get(position).getStatus());
        trigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                /*data init*/
                PendingIntent wakeup = null;
                PendingIntent sleepAssist = null;
                Calendar calendar = Calendar.getInstance();
                Calendar currentTime = Calendar.getInstance();
                alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                Intent intent = getIntent(position);

                calendar.set(Calendar.HOUR_OF_DAY, mData.get(position).getHour());
                calendar.set(Calendar.MINUTE, mData.get(position).getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                /*deal with sleepAssist*/
                Calendar sleepCalender = Calendar.getInstance();
                Intent sleepIntent = new Intent(mContext, FloatingImageDisplayService.class);
                sleepIntent.putExtra("work", 65 * 1000);
                sleepIntent.putExtra("sleep", 1);
                if (mData.get(position).getSleepFlag()) {
                    sleepCalender.set(Calendar.HOUR_OF_DAY, mData.get(position).getSleepHour());
                    sleepCalender.set(Calendar.MINUTE, mData.get(position).getSleepMinute());
                    sleepCalender.set(Calendar.SECOND, 0);
                    sleepCalender.set(Calendar.MILLISECOND, 0);
                }

                if (isChecked) {
                    modifyState(true, position);
                    /*deal with frequency*/
                    if (mData.get(position).getFrequency().get(0)) {
                        System.out.println("无重复闹钟");
                        if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                            calendar.setTimeInMillis(
                                    calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                        }
                        int requestCode = position * 10;
                        wakeup = PendingIntent
                                .getActivity(mContext, requestCode, intent, 0);
                        alarmManager
                                .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), wakeup);

                        if (mData.get(position).getSleepFlag()) {
                            if (sleepCalender.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                                sleepCalender.setTimeInMillis(
                                        sleepCalender.getTimeInMillis() + 24 * 60 * 60 * 1000);
                            }
                            requestCode = -position * 10;
                            sleepAssist = PendingIntent
                                    .getService(mContext, requestCode, sleepIntent, 0);
                            alarmManager
                                    .set(AlarmManager.RTC_WAKEUP, sleepCalender.getTimeInMillis(),
                                         sleepAssist);
                        }
                    } else {
                        System.out.println("每周闹钟");
                        int currentWeekday = calendar.get(Calendar.DAY_OF_WEEK);
                        int setWeekday = currentWeekday;
                        for (int i = 1; i <= 7; i++) {
                            if (mData.get(position).getFrequency().get(i)) {
                                setWeekday = (i + 1) % 7;
                                int delta_day = setWeekday - currentWeekday;
                                if (delta_day < 0) {
                                    delta_day += 7;
                                }
                                long start_time = calendar.getTimeInMillis()
                                        + 24 * 60 * 60 * 1000 * delta_day;
                                int requestCode = position * 10 + i;
                                wakeup = PendingIntent
                                        .getActivity(mContext, requestCode, intent, 0);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time,
                                                          24 * 60 * 60 * 1000 * 7, wakeup);

                                if (mData.get(position).getSleepFlag()) {
                                    start_time = sleepCalender.getTimeInMillis()
                                            + 24 * 60 * 60 * 1000 * (delta_day - 1);
                                    requestCode = -(position * 10 + i);
                                    sleepAssist = PendingIntent
                                            .getService(mContext, requestCode, sleepIntent, 0);
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time,
                                                              24 * 60 * 60 * 1000 * 7, sleepAssist);
                                }
                            }
                        }
                    }
                } else {
                    modifyState(false, position);
                    if (mData.get(position).getFrequency().get(0)) {
                        System.out.println("关闭无重复闹钟");
                        int requestCode = position * 10;
                        wakeup = PendingIntent
                                .getActivity(mContext, requestCode, intent, 0);
                        alarmManager.cancel(wakeup);

                        if (mData.get(position).getSleepFlag()) {
                            requestCode = -position * 10;
                            sleepAssist = PendingIntent
                                    .getService(mContext, requestCode, sleepIntent, 0);
                            alarmManager.cancel(sleepAssist);
                        }
                    } else {
                        System.out.println("关闭每周闹钟");
                        for (int i = 1; i <= 7; i++) {
                            if (mData.get(position).getFrequency().get(i)) {
                                int requestCode = position * 10 + i;
                                wakeup = PendingIntent
                                        .getActivity(mContext, requestCode, intent, 0);
                                alarmManager.cancel(wakeup);

                                if (mData.get(position).getSleepFlag()) {
                                    requestCode = -(position * 10 + i);
                                    sleepAssist = PendingIntent
                                            .getService(mContext, requestCode, sleepIntent, 0);
                                    alarmManager.cancel(sleepAssist);
                                }
                            }
                        }
                    }
                }
            }
        });
        return convertView;
    }
}
