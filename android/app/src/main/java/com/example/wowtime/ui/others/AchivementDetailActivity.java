package com.example.wowtime.ui.others;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;

public class AchivementDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_detail_activity);

        Intent intent=getIntent();
        assert intent!=null;
        String titleString=intent.getStringExtra("title");
        String contentString=intent.getStringExtra("content");

        TextView title=findViewById(R.id.achievement_detail_title);
        TextView content=findViewById(R.id.achievement_detail_content);
//        ImageView imageView=findViewById(R.id.achievement_detail_icon);


        title.setText(titleString);
        content.setText(contentString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        return super.onCreateOptionsMenu(menu);
    }
}
