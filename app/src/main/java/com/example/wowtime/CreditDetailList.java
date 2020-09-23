package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.wowtime.adapter.CreditDetailListAdapter;
import com.example.wowtime.component.CreditDetailListItem;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;

public class CreditDetailList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_detail_list);

        Timestamp initTime = new Timestamp(System.currentTimeMillis());
        ArrayList<CreditDetailListItem> creditDetailListItems = new ArrayList<>();
        creditDetailListItems.add(new CreditDetailListItem(initTime, 2, "IncreaseEvent1"));
        creditDetailListItems.add(new CreditDetailListItem(initTime, 3, "IncreaseEvent2"));
        creditDetailListItems.add(new CreditDetailListItem(initTime, -3, "DecreaseEvent3"));
        CreditDetailListAdapter creditDetailListAdapter = new CreditDetailListAdapter(creditDetailListItems, getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.credit_detail_list);
        listView.setAdapter(creditDetailListAdapter);
    }
}