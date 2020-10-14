package com.example.wowtime.ui.others;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;
import com.example.wowtime.adapter.AchievementAdapter;

import java.util.ArrayList;

public class AchievementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_activity);

        ArrayList<String> arr=new ArrayList<>();
        for (int i=0;i<9;i++){
            String title="achievement"+ i;
            arr.add(title);
        }
        AchievementAdapter adapter=new AchievementAdapter(arr,getApplicationContext());
        GridView gridView=findViewById(R.id.AchievementTable);
        gridView.setAdapter(adapter);
    }
}
