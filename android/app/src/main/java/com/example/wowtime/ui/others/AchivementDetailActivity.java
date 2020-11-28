package com.example.wowtime.ui.others;

import android.content.Intent;
import android.content.SharedPreferences;
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
        int position=intent.getIntExtra("position",-1);
        String titleString=intent.getStringExtra("title");
        String contentString=intent.getStringExtra("content");

        TextView title=findViewById(R.id.achievement_detail_title);
        TextView content=findViewById(R.id.achievement_detail_content);
        TextView achieveOrNot=findViewById(R.id.achievement_detail_achieve);
//        ImageView imageView=findViewById(R.id.achievement_detail_icon);

        SharedPreferences achievement=super.getSharedPreferences("achievement",MODE_PRIVATE);
        boolean achieve=false;
        String achieveDetail="";
        switch (position){
            case 0:{
                int count=Integer.parseInt(achievement.getString("friend_have","0"));
                if(count!=0)
                    achieve=true;
                achieveDetail="已添加"+count+"个好友";
                break;
            }
            case 2:{
                int count=Integer.parseInt(achievement.getString("alarm_count","0"));
                if(count>=5)
                    achieve=true;
                achieveDetail="已完成"+count+"次";
                break;
            }
            case 3:{
                int count=Integer.parseInt(achievement.getString("pomodoro_count","0"));
                if(count>=5)
                    achieve=true;
                achieveDetail="已完成"+count+"次";
                break;
            }
            case 4:{
                int count=Integer.parseInt(achievement.getString("friend_have","0"));
                if(count>=10)
                    achieve=true;
                achieveDetail="已添加"+count+"个好友";
                break;
            }
            case 6:{
                int count=Integer.parseInt(achievement.getString("alarm_count","0"));
                if(count>=50)
                    achieve=true;
                achieveDetail="已完成"+count+"次";
                break;
            }
            case 7:{
                int count=Integer.parseInt(achievement.getString("pomodoro_count","0"));
                if(count>=50)
                    achieve=true;
                achieveDetail="已完成"+count+"次";
                break;
            }
            case 8:{
                if((achievement.getString("alarm_sleep","0")).equals("1"))
                    achieve=true;
                break;
            }
            case 9:{
                int focusedMinute=Integer.parseInt(achievement.getString("pomodoro_time_minite","0"));
                if(focusedMinute>=60)
                    achieve=true;
                achieveDetail="已累计专注"+focusedMinute+"分钟";
                break;
            }
            case 10:{
                if((achievement.getString("pomodoro_single_60","0")).equals("1"))
                    achieve=true;
                break;
            }
        }

        title.setText(titleString);
        content.setText(contentString+"\n"+achieveDetail);
        if(achieve)
            achieveOrNot.setText("已获得该成就");
        else
            achieveOrNot.setText("未获得该成就");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        return super.onCreateOptionsMenu(menu);
    }
}
