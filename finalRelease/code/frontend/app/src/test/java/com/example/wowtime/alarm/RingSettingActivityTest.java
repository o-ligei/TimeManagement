package com.example.wowtime.alarm;

import static org.junit.Assert.assertTrue;

import android.widget.ListView;
import com.example.wowtime.R;
import com.example.wowtime.ui.alarm.RingSettingActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RingSettingActivityTest {
    ActivityController<RingSettingActivity> ringSettingActivityController;
    RingSettingActivity ringSettingActivity;

    @Before
    public void init(){
        System.out.println("init activity");
        ringSettingActivityController= Robolectric.buildActivity(RingSettingActivity.class).create();
        ringSettingActivity=ringSettingActivityController.get();
    }

    @Test
    public void testList(){
        System.out.println("begin test list");
        ListView listView = ringSettingActivity.findViewById(R.id.RingSettingList);
        listView.performClick();
        assertTrue(true);
    }
}
