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
    private ArrayList<ArrayList<PendingIntent>> pi;
    private ArrayList<ArrayList<PendingIntent>> sleepPi;

    public AlarmItemAdapter(List<AlarmListItem> mData, Context mContext) {
        pi = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            pi.add(null);
        }
        sleepPi = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            sleepPi.add(null);
        }
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
            txt_frequency.setText("无重复");
        } else {
            Weekday weekday = new Weekday();
            StringBuilder out = new StringBuilder("星期");
            for (int i = 1; i <= 7; i++) {
                if (frequency.get(i)) {
                    out.append(weekday.getDay(i));
                    txt_frequency.setText(out);
                }
            }
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
        trigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, mData.get(position).getHour());
                    calendar.set(Calendar.MINUTE, mData.get(position).getMinute());
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Calendar currentTime = Calendar.getInstance();
                    alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent;
//                    System.out.println(mData.get(position).getGame());

                    /*deal with game*/
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
                    /*deal with frequency*/
                    if (mData.get(position).getFrequency().get(0)) {
                        System.out.println("无重复闹钟");
                        if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                            calendar.setTimeInMillis(
                                    calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                        }
                        int requestCode = position * 10;
                        PendingIntent tmp = PendingIntent
                                .getActivity(mContext, requestCode, intent, 0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), tmp);
                        ArrayList<PendingIntent> arrayPi = new ArrayList<>();
                        for (int j = 0; j < 8; j++) {
                            arrayPi.add(null);
                        }
                        arrayPi.set(0, tmp);
                        pi.set(position, arrayPi);

                        if (mData.get(position).getSleepFlag()) {
                            if (sleepCalender.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                                sleepCalender.setTimeInMillis(
                                        sleepCalender.getTimeInMillis() + 24 * 60 * 60 * 1000);
                            }
                            requestCode = -position * 10;
                            System.out.println("sleepCalender:" + sleepCalender.getTimeInMillis());
                            System.out.println("currentTime:" + currentTime.getTimeInMillis());
                            tmp = PendingIntent.getService(mContext, requestCode, sleepIntent, 0);
                            alarmManager
                                    .set(AlarmManager.RTC_WAKEUP, sleepCalender.getTimeInMillis(),
                                         tmp);
                            arrayPi = new ArrayList<>();
                            for (int j = 0; j < 8; j++) {
                                arrayPi.add(null);
                            }
                            arrayPi.set(0, tmp);
                            sleepPi.set(position, arrayPi);
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
                                System.out.println("Calender:" + start_time);
                                System.out.println("currentTime:" + currentTime.getTimeInMillis());
                                int requestCode = position * 10 + i;
                                PendingIntent tmp = PendingIntent
                                        .getActivity(mContext, requestCode, intent, 0);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time,
                                                          24 * 60 * 60 * 1000 * 7, tmp);
                                ArrayList<PendingIntent> arrayPi = new ArrayList<>();
                                for (int j = 0; j < 8; j++) {
                                    arrayPi.add(null);
                                }
                                arrayPi.set(i, tmp);
                                pi.set(position, arrayPi);

                                if (mData.get(position).getSleepFlag()) {
                                    start_time = sleepCalender.getTimeInMillis()
                                            + 24 * 60 * 60 * 1000 * (delta_day - 1);
                                    System.out.println("sleepCalender:" + start_time);
                                    System.out.println(
                                            "currentTime:" + currentTime.getTimeInMillis());
                                    requestCode = -(position * 10 + i);
                                    tmp = PendingIntent
                                            .getService(mContext, requestCode, sleepIntent, 0);
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time,
                                                              24 * 60 * 60 * 1000 * 7, tmp);
                                    arrayPi = new ArrayList<>();
                                    for (int j = 0; j < 8; j++) {
                                        arrayPi.add(null);
                                    }
                                    arrayPi.set(i, tmp);
                                    sleepPi.set(position, arrayPi);
                                }
                            }
                        }
                    }
                } else {
                    ArrayList<PendingIntent> tmpPi = pi.get(position);
                    ArrayList<PendingIntent> tmpSleepPi = sleepPi.get(position);
                    for (int i = 0; i < 8; i++) {
                        PendingIntent tmp = tmpPi.get(i);
                        if (tmp != null) {
                            alarmManager.cancel(tmp);
                        }
                        tmp = tmpSleepPi.get(position);
                        if (tmp != null) {
                            alarmManager.cancel(tmp);
                        }
                    }
                    pi.set(position, null);
                    sleepPi.set(position, null);
                }
            }
        });
        return convertView;
    }
}
