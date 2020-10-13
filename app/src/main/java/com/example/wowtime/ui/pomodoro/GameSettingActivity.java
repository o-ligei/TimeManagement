package com.example.wowtime.ui.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.RingItemAdapter;

import java.util.ArrayList;

public class GameSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);

        ArrayList<String> arr=new ArrayList<>();
        for (int i=0;i<5;i++){
            String title="game"+ i;
            arr.add(title);
        }

        RingItemAdapter adapter=new RingItemAdapter(arr,getApplicationContext());
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.activity_ring_list_item,arr);
        ListView listView = findViewById(R.id.GameSettingList);
        listView.setAdapter(adapter);
    }
}