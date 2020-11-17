package com.example.wowtime.ui.alarm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;
import com.example.wowtime.adapter.RingItemAdapter;
import java.util.ArrayList;

public class RingSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_setting_activity);

        ArrayList<String> arr = new ArrayList<>();
        arr.add("radar");
        arr.add("classic");
        RingItemAdapter adapter = new RingItemAdapter(arr, getApplicationContext());
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.ring_list_item,arr);
        ListView listView = findViewById(R.id.RingSettingList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences mySharedPreferences = getSharedPreferences("clock",
                                                                             Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
//                System.out.println(taskItemAdapter.getItem(position).toString());
                editor.putString("ring", adapter.getItem(position).toString());
                editor.apply();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        return super.onCreateOptionsMenu(menu);
    }

}