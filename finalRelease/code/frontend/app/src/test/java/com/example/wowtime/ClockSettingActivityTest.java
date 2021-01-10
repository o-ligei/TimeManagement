package com.example.wowtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Button;
import android.widget.TimePicker;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.alarm.RingSettingActivity;
import java.util.Calendar;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ClockSettingActivityTest {
    ActivityController<ClockSettingActivity> clockSettingActivityController;
    ClockSettingActivity clockSettingActivity;

    @Before
    public void init(){
        System.out.println("init activity");
        Intent intent = new Intent();
        intent.putExtra("position", -1);
        clockSettingActivityController = Robolectric.buildActivity(ClockSettingActivity.class,intent).create();
        clockSettingActivity = clockSettingActivityController.get();
    }

    @Test
    public void testClockRingSettingBtn(){
        System.out.println("begin test ring setting button");
        Button testButton=clockSettingActivity.findViewById(R.id.ClockRingSetting);
        testButton.performClick();
        Intent expectedIntent = new Intent(clockSettingActivity,RingSettingActivity.class);
        /*use ShadowApplication*/
//        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();

        /*use ShadowActivity*/
        ShadowActivity shadowActivity = Shadows.shadowOf(clockSettingActivity);
        Intent actual = shadowActivity.getNextStartedActivity();
        System.out.println(actual.getComponent().getClassName());
        assertEquals(expectedIntent.getComponent(),actual.getComponent());
    }

//    @Test
//    public void TestLifeCycle(){
//        clockSettingActivityController.start();
//        clockSettingActivityController.resume();
//
//    }

    @Test
    public void testOnCreate(){
        System.out.println("begin test on create");
        assertEquals(-1, clockSettingActivity.getIntent().getExtras().getInt("position"));
        Button gameSetting = clockSettingActivity.findViewById(R.id.GameSetting);
        assertEquals(clockSettingActivity.getResources().getString(R.string.shaking_game_setting_header),gameSetting.getText());
        Button RingSetting=clockSettingActivity.findViewById(R.id.ClockRingSetting);
        assertEquals(clockSettingActivity.getResources().getString(R.string.alarm_ring_radar),RingSetting.getText());
        TimePicker timePicker=clockSettingActivity.findViewById(R.id.alarmtimePicker);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        assertEquals(hour,timePicker.getHour());
        assertEquals(minute,timePicker.getMinute());
    }

    @Test
    public void testOnRestart(){
        System.out.println("begin test on restart");
        SharedPreferences mySharedPreferences =RuntimeEnvironment.application.getSharedPreferences("clock",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("game", clockSettingActivity.getResources().getString(R.string.blowing_game_setting_header));
        editor.apply();
        clockSettingActivityController.resume();
//        clockSettingActivity=clockSettingActivityController.get();
        Button gameSetting = clockSettingActivity.findViewById(R.id.GameSetting);
        assertEquals(clockSettingActivity.getResources().getString(R.string.blowing_game_setting_header),gameSetting.getText());
    }
}
