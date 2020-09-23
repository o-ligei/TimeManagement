package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.wowtime.adapter.AlarmItemAdapter;
import com.example.wowtime.component.AlarmListItem;

import java.util.ArrayList;

public class AlarmList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        ArrayList<AlarmListItem> test=new ArrayList<>();
        AlarmListItem alarm1=new AlarmListItem("night","23:00");
        AlarmListItem alarm2=new AlarmListItem("morning","06:00");
        AlarmListItem alarm3=new AlarmListItem("weekend","08:00");
        test.add(alarm1);
        test.add(alarm2);
        test.add(alarm3);

        AlarmItemAdapter adapter=new AlarmItemAdapter(test,getApplicationContext());
        ListView listView=findViewById(R.id.AlarmCardList);
        listView.setAdapter(adapter);
    }
}