package com.example.wowtime.ui.pomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.wowtime.MainApplication;
import com.example.wowtime.R;
import com.example.wowtime.dto.PomodoroListItem;
import com.example.wowtime.dto.StatisticDayItem;
import com.example.wowtime.service.Accumulation;
import com.example.wowtime.service.Credit;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;
import com.example.wowtime.util.InternetConstant;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class PomodoroSettingActivity extends AppCompatActivity {

    private SharedPreferences pomodoroSp;
    private int mode = 0;
    TimePicker timePicker, timePicker2, timePicker3;
    EditText editText;
    private boolean first = false;
    private int position;
    private int focusedSeconds;
    private Date begin;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) { System.out.println("Focused!!!!!"); } else {
            System.out.println("Not Focused!!");
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public static String secondToTime(long second) {
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;                //剩余秒数
        return hours + "h" + minutes+"min";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //float window
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startFloatingImageDisplayService(View view, int time, int rest) {
        if (FloatingImageDisplayService.isStarted) {
            System.out.println("Floating window is already start!");
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            try {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Looper.prepare();
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                              Uri.parse("package:" + getPackageName())), 1);
        } else {
            System.out.println("Floating window starts!");
            startService(
                    new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class).
                                                                                                       putExtra(
                                                                                                               "work",
                                                                                                               time)
                                                                                               .putExtra(
                                                                                                       "rest",
                                                                                                       rest));
//            startService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
        }
    }

    //grant authorization of floatingWindow
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
//                startService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void jumpFromExisted() {
        String stringList = pomodoroSp.getString("pomodoroList", "");
        List<PomodoroListItem> pomodoroListItems = JSON
                .parseArray(stringList, PomodoroListItem.class);
        assert (pomodoroListItems != null);

        PomodoroListItem pomodoroListItem = pomodoroListItems.get(position);
        setFromExisted(timePicker, pomodoroListItem.getTotalGap());
        setFromExisted(timePicker2, pomodoroListItem.getWorkGap());
        setFromExisted(timePicker3, pomodoroListItem.getRestGap());
        editText.setText(pomodoroListItem.getName());
        mode = pomodoroListItem.getMode();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFromExisted(TimePicker tmpTimePicker, int time) {
        int hour = time / 60;
        tmpTimePicker.setHour(hour);
        tmpTimePicker.setMinute(time - hour * 60);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void jumpFromCreate() {
        timePicker.setHour(1);
        timePicker.setMinute(10);
        timePicker2.setHour(0);
        timePicker2.setMinute(32);
        timePicker3.setHour(0);
        timePicker3.setMinute(3);
        editText.setText(getResources().getText(R.string.pomodoro_default_name));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int getTimeFromPickerMS(TimePicker tmpTimePicker) {
        int hour = tmpTimePicker.getHour();
        int minite = tmpTimePicker.getMinute();
        return 1000 * (hour * 3600 + minite * 60);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int getTimeFromPickerMIN(TimePicker tmpTimePicker) {
        int hour = tmpTimePicker.getHour();
        int minite = tmpTimePicker.getMinute();
        return hour * 60 + minite;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainApplication.getThemeNumber() == 1) { setTheme(R.style.DarkTheme); }
        setContentView(R.layout.pomodoro_setting_activity);

        //pomodoro.xml
        pomodoroSp = super.getSharedPreferences("pomodoro", MODE_PRIVATE);

        timePicker = findViewById(R.id.PomodorotimePicker);
        timePicker.setIs24HourView(true);
        timePicker2 = findViewById(R.id.PomodorotimePicker2);
        timePicker2.setIs24HourView(true);
        ApkTool.resizePikcer(timePicker2);
        timePicker3 = findViewById(R.id.PomodorotimePicker3);
        timePicker3.setIs24HourView(true);
        ApkTool.resizePikcer(timePicker3);

        if(getResources().getString(R.string.alarm_guide).equals("设置几点入睡")){
            NewbieGuide.with(PomodoroSettingActivity.this)
                       .setLabel("guide1")
//                       .alwaysShow(true)
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(timePicker)
                                              .setLayoutRes(R.layout.phone_guide_1))
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(timePicker2)
                                              .setLayoutRes(R.layout.phone_guide_2))
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(timePicker3)
                                              .setLayoutRes(R.layout.phone_guide_3))
                       .show();
        }else{
            NewbieGuide.with(PomodoroSettingActivity.this)
                       .setLabel("guide1")
//                       .alwaysShow(true)
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(timePicker)
                                              .setLayoutRes(R.layout.phone_guide_en_1))
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(timePicker2)
                                              .setLayoutRes(R.layout.phone_guide_en_2))
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(timePicker3)
                                              .setLayoutRes(R.layout.phone_guide_en_3))
                       .show();
        }

        Spinner spinner = findViewById(R.id.PomodoroModeSpinner);
        TextView spinnerText = findViewById(R.id.PomodoroSelectModeText);

        editText = findViewById(R.id.editName);

        //get data if jumped by clicking existed task
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        if (position != -1) { jumpFromExisted(); } else { jumpFromCreate(); }

        //mode text
        String text = spinner.getItemAtPosition(mode).toString();
        spinnerText.setText(text);

        //spinner select
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {//通过此方法为下拉列表设置点击事件
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i,
                            long l) {
                        if (first) {
                            mode = i;
                            String text = spinner.getItemAtPosition(i).toString();
                            spinnerText.setText(text);
                            System.out.println("PomodoroMode:" + i);
                        } else { first = true; }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        spinnerText.setText(
                                getBaseContext().getResources().getText(R.string.pomodoro_mode));
                    }
                });

        //whiteList button
        TextView textView = findViewById(R.id.PomodoroSelectWhiteListText);
        textView.setOnClickListener(v -> startActivity(
                new Intent(PomodoroSettingActivity.this, WhiteListActivity.class)));

        //begin button
        Button buttonBegin = findViewById(R.id.setPomodoroButton);
        buttonBegin.setOnClickListener(v -> {
            int totalTime = getTimeFromPickerMS(timePicker) / 60;
            int time = getTimeFromPickerMS(timePicker2) / 60;
            int rest = getTimeFromPickerMS(timePicker3) / 60;

            FloatingImageDisplayService.setIsCanceled(false);
            begin = new Date();
            if (rest != 0) { startFloatingImageDisplayService(buttonBegin, time, rest); } else {
                startFloatingImageDisplayService(buttonBegin, totalTime, 0);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                 WindowManager.LayoutParams.FLAG_FULLSCREEN);

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(totalTime);
                    } catch (InterruptedException e) {
                        System.out.println("interrupted???");
                        e.printStackTrace();
                    }
                    System.out.println("cancel");
                    finish();
                    stopService(new Intent(PomodoroSettingActivity.this,
                                           FloatingImageDisplayService.class));
                    focusedSeconds = FloatingImageDisplayService.setIsCanceled(true);
                    FloatingImageDisplayService.setTime(0);
                    System.out.println("cancel successfully?");
                    int times=totalTime/(time+rest);
                    if(focusedSeconds*1000>=totalTime-rest*(times+1)-10000) {
                        System.out.println("jump from pomodoro to success");
                        runOnUiThread(() -> {
                            Credit credit = new Credit();
                            credit.modifyCredit(InternetConstant.away_from_phone, "awayFromPhone");
                            Accumulation accumulation = new Accumulation(getApplicationContext());
                            accumulation.addAccumulation(InternetConstant.away_from_phone);
                            startActivity(new Intent(PomodoroSettingActivity.this,
                                                     TaskSuccessActivity.class)
                                                  .putExtra("description",secondToTime(focusedSeconds)));
                        });
                    }

                    List<StatisticDayItem> statisticDay;
                    String statisticDayString = pomodoroSp.getString("statistic", "");
                    if (statisticDayString.equals("")) { statisticDay = new LinkedList<>(); } else {
                        statisticDay = JSONObject
                                .parseArray(statisticDayString, StatisticDayItem.class);
                    }
                    System.out.println("focusedSeconds:" + focusedSeconds);
                    statisticDay.add(new StatisticDayItem(editText.getText().toString(),
                                                          focusedSeconds / 3600,
                                                          (focusedSeconds % 3600) / 60, begin,
                                                          new Date()));
                    SharedPreferences.Editor editor = pomodoroSp.edit();
                    editor.putString("statistic", JSONObject.toJSONString(statisticDay));
                    editor.apply();
                    //achievement
                    SharedPreferences achievement = PomodoroSettingActivity.super
                            .getSharedPreferences("achievement", MODE_PRIVATE);
                    //次数
                    int count = Integer.parseInt(achievement.getString("pomodoro_count", "0"));
                    //累计时长
                    int time = Integer.parseInt(achievement.getString("pomodoro_time_minite", "0"));
                    count += 1;
                    time += (int) (focusedSeconds / 60);
                    SharedPreferences.Editor editorAchievement = achievement.edit();
                    editorAchievement.putString("pomodoro_count", Integer.toString(count));
                    editorAchievement.putString("pomodoro_time_minite", Integer.toString(time));
                    //单个时长
                    if (focusedSeconds >= 3600) {
                        editorAchievement.putString("pomodoro_single_60", "1");
                    }
                    editorAchievement.apply();
                }
            }.start();

        });

        int ifBegin = intent.getIntExtra("begin", 0);
        if (ifBegin == 1) { buttonBegin.callOnClick(); }

        //save button
        Button buttonSave = findViewById(R.id.PomodoroSettingConfirm);
        buttonSave.setOnClickListener(v -> {
            int totalGap = getTimeFromPickerMIN(timePicker);
            int workGap = getTimeFromPickerMIN(timePicker2);
            int restGap = getTimeFromPickerMIN(timePicker3);
            String name = editText.getText().toString();
            PomodoroListItem pomodoroListItem = new PomodoroListItem(name, totalGap, workGap,
                                                                     restGap, mode);
            SharedPreferences.Editor editor = pomodoroSp.edit();
            String stringList = pomodoroSp.getString("pomodoroList", "");
            System.out.println("pomodoroList:" + stringList);
            List<PomodoroListItem> pomodoroListItems = new LinkedList<>();
            if (stringList != null && !stringList.equals("")) {
                pomodoroListItems = JSON.parseArray(stringList, PomodoroListItem.class);
            }
            if (position == -1) { pomodoroListItems.add(pomodoroListItem); } else {
                pomodoroListItems.set(position, pomodoroListItem);
            }
            editor.putString("pomodoroList", JSONObject.toJSONString(pomodoroListItems));
            editor.commit();
            Toast.makeText(this, getBaseContext().getResources()
                                                 .getText(R.string.pomodoro_save_successfully),
                           Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
