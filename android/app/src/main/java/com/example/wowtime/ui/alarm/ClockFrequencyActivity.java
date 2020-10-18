package com.example.wowtime.ui.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.RingItemAdapter;

import java.util.ArrayList;

public class ClockFrequencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_frequency);
        ArrayList<String> arr=new ArrayList<>();
        arr.add("无重复");
        arr.add("每周一");
        arr.add("每周二");
        arr.add("每周三");
        arr.add("每周四");
        arr.add("每周五");
        arr.add("每周六");
        arr.add("每周日");
        arr.add("每天");
        RingItemAdapter adapter=new RingItemAdapter(arr,getApplicationContext());
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.ring_list_item,arr);
        ListView listView = findViewById(R.id.ClockFrequencyList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences mySharedPreferences= getSharedPreferences("clock", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
//                System.out.println(taskItemAdapter.getItem(position).toString());
                editor.putString("frequency", adapter.getItem(position).toString());
                editor.apply();
                finish();
            }
        });
    }
}