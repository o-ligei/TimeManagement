package com.example.wowtime.ui.alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.adapter.AlarmItemAdapter;
import com.example.wowtime.dto.AlarmListItem;
import com.example.wowtime.service.AddAlarm;
import com.example.wowtime.util.UserInfoAfterLogin;
import com.example.wowtime.util.Weekday;
import java.util.ArrayList;
import java.util.List;

public class AlarmListFragment extends Fragment {

    List<AlarmListItem> alarmList = new ArrayList<>();
    AlarmItemAdapter adapter;
    JSONArray AlarmArray;
    ListView listView;

    public AlarmListFragment() {}

    public AlarmListFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    private class DialogListener implements OnClickListener {

        int dialog_num;
        Context context;

        DialogListener(Context mContext) {
            dialog_num = 0;
            this.context = mContext;
        }

        private void setDialog_num(int num) {
            dialog_num = num;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            System.out.println("ok");
            String item = AlarmArray.get(dialog_num).toString();
            JSONObject json_item = JSONObject.parseObject(item);
            String clocksetting = json_item.getString("clockSetting");
            AlarmListItem alarm = JSONObject.parseObject(clocksetting, AlarmListItem.class);
            AddAlarm addAlarm = new AddAlarm(context, alarm);
            addAlarm.storeAlarm();
            SharedPreferences mySharedPreferences = requireActivity()
                    .getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
            String shared = mySharedPreferences.getString("list", "");
            System.out.println("alarmList:" + shared);
            if (shared == null || shared.equals("")) {
                return;
            }
            alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
            adapter = new AlarmItemAdapter(alarmList, context);
            listView.setAdapter(adapter);
        }
    }

    private class FriendAlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String alarmArray = intent.getStringExtra("alarmArray");
            System.out.println("alarmArray" + alarmArray);
            int k = 0;
            AlarmArray = JSONObject.parseArray(alarmArray);
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                dialog.setMessage("something important");//设置信息具体内容
            dialog.setCancelable(true);//设置是否可取消
//            dialog.setPositiveButton("accept", new DialogInterface.OnClickListener() {
//                @Override//设置ok的事件
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    System.out.println("ok");
//                    final int j=k;
//
//                    //在此处写入ok的逻辑
//                }
//            });
            dialog.setNegativeButton("refuse", new DialogInterface.OnClickListener() {
                @Override//设置取消事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.out.println("cancel");
                    //在此写入取消的事件
                }
            });
            for (k = 0; k < AlarmArray.size(); k++) {
                String item = AlarmArray.get(k).toString();
                JSONObject json_item = JSONObject.parseObject(item);
                String clocksetting = json_item.getString("clockSetting");
                String username = json_item.getString("username");
                dialog.setTitle("from " + username);
                AlarmListItem alarm = JSONObject.parseObject(clocksetting, AlarmListItem.class);
                StringBuilder message =
                        new StringBuilder(
                                getResources().getString(R.string.send_alarm_tag) + alarm.getTag()
                                        + "\n"
                                        + getResources().getString(R.string.send_alarm_time) + alarm
                                        .getHour() + ":" + alarm.getMinute() + "\n" + getResources()
                                        .getString(R.string.send_alarm_game) + alarm.getGame()
                                        + "\n" + getResources()
                                        .getString(R.string.send_alarm_ring) + alarm.getRing()
                                        + "\n");
                boolean flag = getResources()
                        .getString(R.string.alarm_frequency_no_repeat).equals("无重复");
                if (alarm.getFrequency().get(0)) {
                    if (flag) {
                        message.append(getResources().getString(R.string.send_alarm_repeat))
                               .append("无重复\n");
                    } else {
                        message.append(getResources().getString(R.string.send_alarm_repeat))
                               .append("one time\n");
                    }
                } else {
                    Weekday weekday = new Weekday(flag);
                    StringBuilder out;
                    if (flag) {
                        out = new StringBuilder("星期");
                    } else {
                        out = new StringBuilder("");
                    }
                    for (int j = 1; j <= 7; j++) {
                        if (alarm.getFrequency().get(j)) {
                            out.append(weekday.getDay(j));
                            out.append(" ");
                        }
                    }
                    message.append(getResources().getString(R.string.send_alarm_repeat))
                           .append(out).append("\n");
                }
                if (alarm.getSleepFlag()) {
                    if (flag) {
                        message.append(getResources().getString(R.string.send_alarm_assist))
                               .append("开启\n");
                    } else {
                        message.append(getResources().getString(R.string.send_alarm_assist))
                               .append("open\n");
                    }
                    message.append(getResources().getString(R.string.send_alarm_assist_time))
                           .append(alarm.getSleepHour()).append(":").append(alarm.getMinute())
                           .append("\n");
                } else {
                    if (flag) {
                        message.append(getResources().getString(R.string.send_alarm_assist))
                               .append("关闭\n");
                    } else {
                        message.append(getResources().getString(R.string.send_alarm_assist))
                               .append("closed\n");
                    }
                }
                dialog.setMessage(message);
                DialogListener dialogListener = new DialogListener(context);
                dialogListener.setDialog_num(k);
                dialog.setPositiveButton("accept", dialogListener);
                dialog.show();
            }
        }
    }

    private FriendAlarmReceiver friendAlarmReceiver;
    private IntentFilter intentFilter;

    private void doAlarmReceiver() {
        friendAlarmReceiver = new FriendAlarmReceiver();
        intentFilter = new IntentFilter("friend alarm");
        requireActivity().registerReceiver(friendAlarmReceiver, intentFilter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.alarm_list_fragment, container, false);
        System.out.println(UserInfoAfterLogin.userid);
        listView = root.findViewById(R.id.AlarmCardList);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        doAlarmReceiver();
        SharedPreferences mySharedPreferences = requireActivity()
                .getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
        String shared = mySharedPreferences.getString("list", "");
        System.out.println("alarmList:" + shared);
        if (shared == null || shared.equals("")) {
            return;
        }
        alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
        adapter = new AlarmItemAdapter(alarmList, getContext());
//        listView = requireView().findViewById(R.id.AlarmCardList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
//        SharedPreferences mySharedPreferences= requireActivity().getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySharedPreferences.edit();
//        editor.clear();
//        editor.apply();

        requireActivity().unregisterReceiver(friendAlarmReceiver);
    }

}