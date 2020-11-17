package com.example.wowtime.ui.alarm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.adapter.AlarmItemAdapter;
import com.example.wowtime.dto.AlarmListItem;
import java.util.ArrayList;
import java.util.List;

public class AlarmListFragment extends Fragment {

    List<AlarmListItem> alarmList = new ArrayList<>();

    public AlarmListFragment() {}

    public AlarmListFragment(int contentLayoutId) {
        super(contentLayoutId);
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

//        Calendar calendar= Calendar.getInstance();
//        AlarmManager alarmManager= (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
//        Intent sleepIntent=new Intent(getContext(), FloatingImageDisplayService.class);
//        sleepIntent.putExtra("work",1000*65).putExtra("sleep",1);
//        PendingIntent tmp= PendingIntent.getService(getContext(),0,sleepIntent,0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+3000, tmp);
//        PendingIntent tmp2= PendingIntent.getService(getContext(),1,sleepIntent,0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+30000,tmp2);

//        AlarmItemAdapter adapter = new AlarmItemAdapter(alarmList, getContext());
//        ListView listView = root.findViewById(R.id.AlarmCardList);
//        listView.setAdapter(adapter);
//        System.out.println(Uri.parse("R.raw.radar"));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mySharedPreferences = requireActivity()
                .getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
        String shared = mySharedPreferences.getString("list", "");
        System.out.println("alarmList:" + shared);
        if (shared == null || shared.equals("")) {
            return;
        }
        alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
        AlarmItemAdapter adapter = new AlarmItemAdapter(alarmList, getContext());
        ListView listView = requireView().findViewById(R.id.AlarmCardList);
        listView.setAdapter(adapter);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        SharedPreferences mySharedPreferences= requireActivity().getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//    }

}