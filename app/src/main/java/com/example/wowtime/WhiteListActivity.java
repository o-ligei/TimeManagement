package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.wowtime.adapter.AppAdapter;

import java.util.LinkedList;
import java.util.List;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.adapter.AppAdapter;
import com.example.wowtime.component.AppListItem;

import java.util.LinkedList;

public class WhiteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_list);

        LinkedList<AppListItem> appListItems = new LinkedList<>();

        appListItems.add(new AppListItem("WeChat",R.drawable.wechat));
        appListItems.add(new AppListItem("QQ",R.drawable.qq));

        AppAdapter appAdapter = new AppAdapter(appListItems,getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.white_list);
        listView.setAdapter(appAdapter);
    }
}