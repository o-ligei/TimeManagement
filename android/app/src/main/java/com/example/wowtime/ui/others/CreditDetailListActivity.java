package com.example.wowtime.ui.others;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.CreditDetailListAdapter;
import com.example.wowtime.dto.CreditDetailListItem;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CreditDetailListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_detail_list_activity);

        Timestamp initTime = new Timestamp(System.currentTimeMillis());
        ArrayList<CreditDetailListItem> creditDetailListItems = new ArrayList<>();
        creditDetailListItems.add(new CreditDetailListItem(initTime, 2, "IncreaseEvent1"));
        creditDetailListItems.add(new CreditDetailListItem(initTime, 3, "IncreaseEvent2"));
        creditDetailListItems.add(new CreditDetailListItem(initTime, -3, "DecreaseEvent3"));
        CreditDetailListAdapter creditDetailListAdapter = new CreditDetailListAdapter(creditDetailListItems, getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.credit_detail_list);
        listView.setAdapter(creditDetailListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
}