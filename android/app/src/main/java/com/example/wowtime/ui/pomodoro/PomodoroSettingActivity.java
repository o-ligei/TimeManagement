package com.example.wowtime.ui.pomodoro;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.PomodoroListItem;
import com.example.wowtime.dto.StatisticDayItem;
import com.example.wowtime.dto.StatisticSimple;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;


public class PomodoroSettingActivity extends AppCompatActivity {

//    private FloatView mFloatView;
    private SharedPreferences pomodoroSp;
    private int mode=0;
    private boolean first=false;
    private int position;
    private Timer timer,timer2;
    private TimerTask timerTask,timerTask2;
    private int focusedSeconds;
    private Date begin;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus)
            System.out.println("Focused!!!!!");
        else
            System.out.println("Not Focused!!");
        super.onWindowFocusChanged(hasFocus);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro_setting_activity);

        //pomodoro.xml
        pomodoroSp=super.getSharedPreferences("pomodoro",MODE_PRIVATE);

        TimePicker timePicker=findViewById(R.id.PomodorotimePicker);
        timePicker.setIs24HourView(true);

        TimePicker timePicker2=findViewById(R.id.PomodorotimePicker2);
        timePicker2.setIs24HourView(true);

        TimePicker timePicker3=findViewById(R.id.PomodorotimePicker3);
        timePicker3.setIs24HourView(true);

        //spinner
        Spinner spinner=findViewById(R.id.PomodoroModeSpinner);
        TextView spinnerText=findViewById(R.id.PomodoroSelectModeText);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//通过此方法为下拉列表设置点击事件
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(first) {
                    mode = i;
                    String text = spinner.getItemAtPosition(i).toString();
                    spinnerText.setText(text);
                    System.out.println("PomodoroMode:" + i);
                }else first=true;
//                Toast.makeText(PomodoroSettingActivity.this,text,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerText.setText( (String) getBaseContext().getResources().getText(R.string.pomodoro_mode));
            }
        });

        EditText editText=findViewById(R.id.editName);

        //resize timePicker
        resizePikcer(timePicker2);
        resizePikcer(timePicker3);

        timePicker.setHour(1);
        timePicker.setMinute(10);
        timePicker2.setHour(0);
        timePicker2.setMinute(30);
        timePicker3.setHour(0);
        timePicker3.setMinute(5);
        editText.setText(getResources().getText(R.string.pomodoro_default_name));

        //get data if jumped by clicking existed task
        Intent intent=getIntent();
        position=intent.getIntExtra("position",-1);
        if(position!=-1){
            String stringList=pomodoroSp.getString("pomodoroList","");
            //Serializable
            List<PomodoroListItem> pomodoroListItems=  JSON.parseArray(stringList,PomodoroListItem.class);
            assert(pomodoroListItems!=null);
            PomodoroListItem pomodoroListItem=pomodoroListItems.get(position);
            int totalGap=pomodoroListItem.getTotalGap();
            int workGap=pomodoroListItem.getWorkGap();
            int restGap=pomodoroListItem.getRestGap();
            int modeGet=pomodoroListItem.getMode();
            int hour1,hour2,hour3,min1,min2,min3;
            if(totalGap<60)
                hour1=0;
            else hour1=totalGap/60;
            min1=totalGap-hour1*60;

            if(workGap<60)
                hour2=0;
            else hour2=workGap/60;
            min2=workGap-hour2*60;

            if(restGap<60)
                hour3=0;
            else hour3=restGap/60;
            min3=restGap-hour3*60;

            timePicker.setMinute(min1);timePicker.setHour(hour1);
            timePicker2.setMinute(min2);timePicker2.setHour(hour2);
            timePicker3.setMinute(min3);timePicker3.setHour(hour3);
            editText.setText(pomodoroListItem.getName());
            mode=modeGet;
        }

        //whiteList button
        TextView textView=findViewById(R.id.PomodoroSelectWhiteListText);
        textView.setOnClickListener(v->startActivity(new Intent(PomodoroSettingActivity.this,WhiteListActivity.class)));
//        getAppList();

        //begin button
        Button buttonBegin=findViewById(R.id.setPomodoroButton);
        buttonBegin.setOnClickListener(v->{
            int minute=timePicker.getMinute();
            int hour=timePicker.getHour();
            int totalTime=minute*60*1000+hour*3600*1000;
            int minute2=timePicker2.getMinute();
            int hour2=timePicker2.getHour();
            int time=minute2*60*1000+hour2*3600*1000;
            int minute3=timePicker3.getMinute();
            int hour3=timePicker3.getHour();
            int rest=minute3*60*1000+hour3*3600*1000;

            totalTime/=60;
            time/=60;
            rest/=60;

            timer=new Timer();
            timer2=new Timer();
            timerTask=new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(()->{startFloatingImageDisplayService(buttonBegin);});
                    System.out.println("begin");
                }
            };
            int finalRest = rest;
            timerTask2=new TimerTask() {
                @Override
                public void run() {
                    stopFloatingImageDisplayService(buttonBegin);
                    System.out.println("stop");
                    int SCAN_INTERVAL = 5000;
                    for (int i = 0; i< finalRest /SCAN_INTERVAL-2; i++) {
                        String currentApp = getTaskPackname();
                        System.out.println("Current Runnning: "+currentApp);
                        if (currentApp.equals("CurrentNULL")) startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                        if (currentApp.equals("com.netease.cloudmusic"))
                            runOnUiThread(()->{startFloatingImageDisplayService(buttonBegin);});
                        try {
                            Thread.sleep(SCAN_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            FloatingImageDisplayService.setIsCanceled(false);
            timer.schedule(timerTask,0,rest+time);
            timer2.schedule(timerTask2,time,rest+time);
            begin=new Date();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);

            int finalTotalTime = totalTime;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(finalTotalTime);
                    } catch (InterruptedException e) {
                        System.out.println("interrupted???");
                        e.printStackTrace();
                    }
                    System.out.println("cancel");
                    timer.cancel();
                    timerTask.cancel();
                    timer2.cancel();
                    timerTask2.cancel();
                    stopFloatingImageDisplayService(buttonBegin);
                    focusedSeconds=FloatingImageDisplayService.setIsCanceled(true);
                    FloatingImageDisplayService.setTime(0);
                    System.out.println("cancel successfully?");
                    runOnUiThread(()->{startActivity(new Intent(PomodoroSettingActivity.this, TaskSuccessActivity.class));});

                    List<StatisticDayItem> statisticDay;
                    List<StatisticSimple> weekUnresolved,yearUnresolved;
                    String statisticDayString=pomodoroSp.getString("statisticDay","");
                    String statisticWeekString=pomodoroSp.getString("unresolvedWeek","");
                    String statisticYearString=pomodoroSp.getString("unresolvedYear","");
                    System.out.println(statisticDayString);

                    if(statisticDayString.equals(""))
                        statisticDay=new LinkedList<>();
                    else
                        statisticDay=JSONObject.parseArray(statisticDayString,StatisticDayItem.class);
                    if(statisticWeekString.equals(""))
                        weekUnresolved=new LinkedList<>();
                    else
                        weekUnresolved=JSONObject.parseArray(statisticWeekString, StatisticSimple.class);
                    if(statisticYearString.equals(""))
                        yearUnresolved=new LinkedList<>();
                    else
                        yearUnresolved=JSONObject.parseArray(statisticYearString, StatisticSimple.class);

                    System.out.println("focusedSeconds:"+focusedSeconds);
                    statisticDay.add(new StatisticDayItem(editText.getText().toString(),focusedSeconds/3600,
                                    (focusedSeconds%3600)/60,begin,new Date()));
                    Calendar c=Calendar.getInstance();
                    c.setTime(new Date());
                    float hourSaved=(float)focusedSeconds/3600;
                    weekUnresolved.add(new StatisticSimple((float) (Math.round(hourSaved*100))/100,c));
                    yearUnresolved.add(new StatisticSimple((float) (Math.round(hourSaved*100))/100,c));

                    SharedPreferences.Editor editor=pomodoroSp.edit();
                    editor.putString("statisticDay",JSONObject.toJSONString(statisticDay));
                    editor.putString("unresolvedWeek",JSONObject.toJSONString(weekUnresolved));
                    editor.putString("unresolvedYear",JSONObject.toJSONString(yearUnresolved));
                    editor.apply();
                    finish();
                }
            }.start();

        });
        int ifBegin=0;
        ifBegin=intent.getIntExtra("begin",0);
        if(ifBegin==1)
            buttonBegin.callOnClick();

        //save button
        Button buttonSave=findViewById(R.id.PomodoroSettingConfirm);
        buttonSave.setOnClickListener(v->{
            int minute=timePicker.getMinute();
            int hour=timePicker.getHour();
            int totalGap=minute+hour*60;
            int minute2=timePicker2.getMinute();
            int hour2=timePicker2.getHour();
            int workGap=minute2+hour2*60;
            int minute3=timePicker3.getMinute();
            int hour3=timePicker3.getHour();
            int restGap=minute3+hour3*60;
            String name=editText.getText().toString();
            PomodoroListItem pomodoroListItem=new PomodoroListItem(name,totalGap,workGap,restGap,mode);
            SharedPreferences.Editor editor=pomodoroSp.edit();
            String stringList=pomodoroSp.getString("pomodoroList","");
            System.out.println("pomodoroList:"+stringList);
            List<PomodoroListItem> pomodoroListItems = new LinkedList<>();
            if (stringList != null && !stringList.equals("")) {
                System.out.println("You Are Here.");
                pomodoroListItems = JSON.parseArray(stringList, PomodoroListItem.class);
            }
            if(position==-1)pomodoroListItems.add(pomodoroListItem);
            else pomodoroListItems.set(position,pomodoroListItem);
            editor.putString("pomodoroList",JSONObject.toJSONString(pomodoroListItems));
            editor.commit();
            Toast.makeText(this, getBaseContext().getResources().getText(R.string.pomodoro_save_successfully),Toast.LENGTH_LONG).show();
            finish();
        });

        //mode text
        String text= spinner.getItemAtPosition(mode).toString();
        spinnerText.setText(text);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private String getTaskPackname() {
        String currentApp = "CurrentNULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        return currentApp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (timer != null)
//            timer.cancel();
//        if (timer2 != null)
//            timer2.cancel();
//        if (timerTask != null)
//            timerTask.cancel();
//        if (timerTask2 != null)
//            timerTask2.cancel();
    }


    /**
     * 调整FrameLayout大小
     * @param tp
     */
    private void resizePikcer(FrameLayout tp){
        List<NumberPicker> npList = findNumberPicker(tp);
        for(NumberPicker np:npList){
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i = 0;i<viewGroup.getChildCount();i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }
                else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }

     // 调整numberpicker大小
    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 400);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }

    //float window
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startFloatingImageDisplayService(View view) {
        if (FloatingImageDisplayService.isStarted) {
            System.out.println("Floating window is already start!" );
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            try {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Looper.prepare();
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1);
        } else {
            System.out.println("Floating window starts!" );
            startService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
        }
    }

    public void stopFloatingImageDisplayService(View view) {
        if (!FloatingImageDisplayService.isStarted) {
            System.out.println("Floating window is already stopped!" );
            return;
        }
        System.out.println("Floating window stops!" );
        stopService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
    }

    //grant authorization of floatingWindow
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
            }
        } else if (requestCode == 1) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
            }
        } else if (requestCode == 2) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
            }
        }
    }

    //some unused function

    // 如下代码直接跳到该应用的悬浮窗权限设置页面
    private Boolean requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 111);
                return false;
            } else {
                System.out.println("OVERLAY_PERMISSION:OK");
                return true;
            }
        }
        return false;
    }

    public static boolean isRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : list) {
            String processName = appProcess.processName;
            if (processName != null && processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

//
//      //some wrong try
//    private void prohibitDropDown() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            System.out.println("cannot get permission of status bar");
//            if (!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, 1);
//            } else {
//                System.out.println("can get permission of status bar");
//                //TODO 做你需要的事情
//                WindowManager managerBar = ((WindowManager) getApplicationContext()
//                        .getSystemService(Context.WINDOW_SERVICE));
//                WindowManager.LayoutParams localLayoutParamsBar = new WindowManager.LayoutParams();
//                localLayoutParamsBar.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//                localLayoutParamsBar.gravity = Gravity.TOP;
//                localLayoutParamsBar.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
//                // 这是为了使通知能够接收触摸事件
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                // 绘制状态栏
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//                localLayoutParamsBar.width = WindowManager.LayoutParams.MATCH_PARENT;
//                localLayoutParamsBar.height = (int) (50 * getResources()
//                        .getDisplayMetrics().scaledDensity);
//                localLayoutParamsBar.format = PixelFormat.TRANSPARENT;
//                View viewBar = new customViewGroup(this);
//                managerBar.addView(viewBar, localLayoutParamsBar);
//            }
//        }
//
//    }
}
