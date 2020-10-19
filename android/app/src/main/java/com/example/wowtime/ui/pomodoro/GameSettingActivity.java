package com.example.wowtime.ui.pomodoro;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.RingItemAdapter;

import java.util.ArrayList;

public class GameSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_setting_activity);

        ArrayList<String> arr=new ArrayList<>();
        for (int i=0;i<5;i++){
            String title="game"+ i;
            arr.add(title);
        }

        RingItemAdapter adapter=new RingItemAdapter(arr,getApplicationContext());
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.ring_list_item,arr);
        ListView listView = findViewById(R.id.GameSettingList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
}