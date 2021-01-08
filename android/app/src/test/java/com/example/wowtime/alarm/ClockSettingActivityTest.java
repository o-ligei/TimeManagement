package com.example.wowtime.alarm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Button;
import android.widget.TimePicker;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.AlarmListItem;
import com.example.wowtime.service.AddAlarm;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.alarm.RingSettingActivity;
import com.example.wowtime.ui.alarm.TaskListActivity;
import com.example.wowtime.ui.pomodoro.GameSettingActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
        String tag = RuntimeEnvironment.application.getResources().getString(R.string.clock_setting_tag_default);
        String game = RuntimeEnvironment.application.getResources().getString(R.string.shaking_game_setting_header);
        String ring = RuntimeEnvironment.application.getResources().getString(R.string.alarm_ring_radar);
        List<Boolean> frequency = new ArrayList<>();
        frequency.add(0, true);
        for (int i = 1; i < 8; i++) {
            frequency.add(i, false);
        }
        Calendar calendar = Calendar.getInstance();
        int Hour = calendar.get(Calendar.HOUR_OF_DAY);
        int Minute = calendar.get(Calendar.MINUTE);
        AlarmListItem alarm=new AlarmListItem(tag,frequency,game,ring,Hour,Minute);
        AddAlarm addAlarm=new AddAlarm(RuntimeEnvironment.application,alarm);
        addAlarm.storeAlarm();

        Intent intent = new Intent();
        intent.putExtra("position", 0);
        clockSettingActivityController = Robolectric.buildActivity(ClockSettingActivity.class,intent).create();
        clockSettingActivity = clockSettingActivityController.get();
    }


    @Test
    public void testOnCreate(){
        System.out.println("begin test on create");
        assertEquals(0, clockSettingActivity.getIntent().getExtras().getInt("position"));
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
    public void testClockRingSettingBtn(){
        System.out.println("begin test ring setting button");
        Button testButton=clockSettingActivity.findViewById(R.id.ClockRingSetting);
        testButton.performClick();
        Intent expectedIntent = new Intent(clockSettingActivity,RingSettingActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(clockSettingActivity);
        Intent actual = shadowActivity.getNextStartedActivity();
//        System.out.println(actual.getComponent().getClassName());
        assertEquals(expectedIntent.getComponent(),actual.getComponent());
    }

    @Test
    public void testGameRingSettingBtn(){
        System.out.println("begin test game setting button");
        Button testButton=clockSettingActivity.findViewById(R.id.GameSetting);
        testButton.performClick();
        Intent expectedIntent = new Intent(clockSettingActivity, TaskListActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(clockSettingActivity);
        Intent actual = shadowActivity.getNextStartedActivity();
        System.out.println(actual.getComponent().getClassName());
        assertEquals(expectedIntent.getComponent(),actual.getComponent());
    }

    @Test
    public void testCancelBtn(){
        System.out.println("begin test cancel button");
        Button testButton=clockSettingActivity.findViewById(R.id.ClockSettingCancel);
        testButton.performClick();
        assertTrue(true);
    }

    @Test
    public void testTimePicker(){
        System.out.println("begin test time picker");
        TimePicker timePicker=clockSettingActivity.findViewById(R.id.alarmtimePicker);
        timePicker.setHour(0);
        assertEquals(timePicker.getHour(),0);
    }

    @Test
    public void testSaveBtn(){
        System.out.println("begin test save button");
        Button saveClock = clockSettingActivity.findViewById(R.id.ClockSettingConfirm);
        saveClock.performClick();
        SharedPreferences mySharedPreferences =RuntimeEnvironment.application.getSharedPreferences("alarmList",Activity.MODE_PRIVATE);
        String shared = mySharedPreferences.getString("list", "");
        List<AlarmListItem> alarmList = new ArrayList<>();
        if (!shared.equals("") && shared != null) {
            alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
        }

        assertEquals(RuntimeEnvironment.application.getResources().getString(R.string.shaking_game_setting_header),alarmList.get(0).getGame());
    }

    @Test
    public void testSendClock(){
        Intent intent = new Intent();
        intent.putExtra("position", -1);
        intent.putExtra("action","set for friend");
        clockSettingActivityController = Robolectric.buildActivity(ClockSettingActivity.class,intent).create();
        clockSettingActivity = clockSettingActivityController.get();

        Button saveClock = clockSettingActivity.findViewById(R.id.ClockSettingConfirm);
        assertEquals(RuntimeEnvironment.application.getResources().getString(R.string.send_alarm),saveClock.getText());

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

    @Test
    public void testOnDestroy(){
        clockSettingActivityController.destroy();
        SharedPreferences mySharedPreferences =RuntimeEnvironment.application.getSharedPreferences("clock",Activity.MODE_PRIVATE);
        String game=mySharedPreferences.getString("game", "");
        assertEquals("",game);
    }


}
