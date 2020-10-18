package com.example.wowtime.adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.dto.AlarmListItem;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.alarm.AlarmPlay;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmItemAdapter extends BaseAdapter {
    private ArrayList<AlarmListItem> mData;
    private Context mContext;
    private AlarmManager alarmManager;
    private PendingIntent pi;

    public AlarmItemAdapter(ArrayList<AlarmListItem> mData, Context mContext) {

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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item,parent,false);

        TextView txt_tag = (TextView) convertView.findViewById(R.id.AlarmTag);
        TextView txt_time=(TextView) convertView.findViewById(R.id.AlarmTime);
        txt_tag.setText(mData.get(position).getTag());
        String hour=String.format("%02d",mData.get(position).getHour());
        String minute=String.format("%02d",mData.get(position).getMinute());
        txt_time.setText(hour+":"+minute);

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
                    if (calendar.getTimeInMillis()<=currentTime.getTimeInMillis()){
                        calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
                    }
                    alarmManager= (AlarmManager) (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(mContext, AlarmPlay.class);
                    intent.putExtra("ring",mData.get(position).getRing());
                    pi=PendingIntent.getActivity(mContext, 0, intent, 0);
//                    if(mData.get(position).getFrequency().equals("无重复")) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
//                    }
//                    else if(mData.get(position).getFrequency().equals("每天")){
//                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),24*60*60*1000, pi);
//                    }
//                    else{
//                        int weekday=calendar.get(Calendar.DAY_OF_WEEK);
//                        if(mData.get(position).getFrequency().equals("每周一")){
//                            long time=
//                        }
//                    }
//                }else{
//                    alarmManager.cancel(pi);
                }
            }
        });
        return convertView;
    }
}
