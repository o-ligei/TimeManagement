package com.example.wowtime.ui.pomodoro;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.LinkedList;
import java.util.List;
import android.os.Bundle;
import android.widget.ListView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.AppItemAdapter;

import java.util.LinkedList;

import com.example.wowtime.component.AppListItem;

public class WhiteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_list);

        LinkedList<AppListItem> appListItems = new LinkedList<>();

        appListItems.add(new AppListItem("WeChat",R.drawable.wechat));
        appListItems.add(new AppListItem("QQ",R.drawable.qq));

        AppItemAdapter appItemAdapter = new AppItemAdapter(appListItems,getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.white_list);
        listView.setAdapter(appItemAdapter);
    }
}