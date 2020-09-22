package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class RingSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_setting);

        Toolbar toolbar=findViewById(R.id.RingSettingToolbar);
        toolbar.setTitle("test");
//        ArrayList<String> arr=new ArrayList<>();
//        for (int i=0;i<20;i++){
//            String title="ring"+ i;
//            arr.add(title);
//        }
//        String[] arr={"string1","string2","3","4"};
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.activity_ring_list_item,arr);
//        ListView listView = findViewById(R.id.RingSettingList);
//        listView.setAdapter(adapter);
    }
}