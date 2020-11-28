package com.example.wowtime.ui.others;

import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;
import com.example.wowtime.adapter.AchievementAdapter;
import java.util.ArrayList;

public class AchievementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_activity);

        ArrayList<String> arr = new ArrayList<>();
//        for (int i = 0; i < 9; i++) {
//            String title = "achievement" + i;
//            arr.add(title);
//        }
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_friend_0));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_share_0));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_alarm_0));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_pomodoro_0));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_friend_1));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_share_1));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_alarm_1));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_pomodoro_1));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_sleep));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_pomodoro_time_0));
        arr.add((String)getBaseContext().getResources().getText(R.string.achievement_header_pomodoro_time_1));

        ArrayList<String> arrContent = new ArrayList<>();
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_friend_0));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_share_0));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_alarm_0));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_pomodoro_0));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_friend_1));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_share_1));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_alarm_1));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_pomodoro_1));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_sleep));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_pomodoro_time_0));
        arrContent.add((String)getBaseContext().getResources().getText(R.string.achievement_content_pomodoro_time_1));

        AchievementAdapter adapter = new AchievementAdapter(arr,arrContent, getApplicationContext());
        GridView gridView = findViewById(R.id.AchievementTable);
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        return super.onCreateOptionsMenu(menu);
    }
}
