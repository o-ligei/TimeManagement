package com.example.wowtime.ui.alarm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;
import com.example.wowtime.adapter.FrequencyItemAdapter;
import java.util.ArrayList;

public class ClockFrequencyActivity extends AppCompatActivity {

    private boolean flag;
    private FrequencyItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_frequency);

        flag = false;

        ArrayList<String> arr = new ArrayList<>();
        arr.add("无重复");
        arr.add("每周一");
        arr.add("每周二");
        arr.add("每周三");
        arr.add("每周四");
        arr.add("每周五");
        arr.add("每周六");
        arr.add("每周日");
//        arr.add("每天");

//        RingItemAdapter adapter=new RingItemAdapter(arr,getApplicationContext());
        adapter = new FrequencyItemAdapter(arr, getApplicationContext());
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.ring_list_item,arr);
        ListView listView = findViewById(R.id.ClockFrequencyList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SharedPreferences mySharedPreferences= getSharedPreferences("clock", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editor = mySharedPreferences.edit();
////                System.out.println(taskItemAdapter.getItem(position).toString());
//                editor.putString("frequency", adapter.getItem(position).toString());
//                editor.apply();
//                finish();
//            }
//        });

        Button btn = findViewById(R.id.FrequencySettingBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sendOptions();
                finish();
            }
        });
    }

}