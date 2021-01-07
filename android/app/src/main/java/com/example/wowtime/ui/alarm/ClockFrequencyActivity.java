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
        arr.add(getResources().getString(R.string.alarm_frequency_no_repeat));
        arr.add(getResources().getString(R.string.alarm_frequency_Monday));
        arr.add(getResources().getString(R.string.alarm_frequency_Tuesday));
        arr.add(getResources().getString(R.string.alarm_frequency_Wednesday));
        arr.add(getResources().getString(R.string.alarm_frequency_Thursday));
        arr.add(getResources().getString(R.string.alarm_frequency_Friday));
        arr.add(getResources().getString(R.string.alarm_frequency_Saturday));
        arr.add(getResources().getString(R.string.alarm_frequency_Sunday));
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