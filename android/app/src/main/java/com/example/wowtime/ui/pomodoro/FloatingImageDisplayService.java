package com.example.wowtime.ui.pomodoro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.service.Accumulation;
import com.example.wowtime.service.Credit;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FloatingImageDisplayService extends Service {
    public static boolean isStarted = false;
    private static boolean isCanceled = false;    //when canceled, cannot be start again
    private static int time = 0;  // notice that it's statistic

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;

    private Timer timingTimer, workTimer, restTimer,monitorTimer;
    private TimerTask timingTask, workTimerTask, restTimerTask, monitorTimerTask;
    private static Handler timingHandler;

    private List<String> whitelist;
    private SharedPreferences pomodoroSp;
    private boolean isInRest;

    @Override
    public void onCreate() {
        super.onCreate();

        if (isCanceled) {
            System.out.println("canceled");
            return;
        }

        isStarted = true;
        System.out.println("floatingService is creating");
        //get whitelist
        pomodoroSp = super.getSharedPreferences("pomodoro", MODE_PRIVATE);
        String s = pomodoroSp.getString("whitelist", "");
        System.out.println("whitelistGetInFloatingWindow:" + s);
        if (s.equals(""))
            whitelist = new LinkedList<>();
        else
            whitelist = JSONObject.parseArray(s, String.class);
        //get windowManager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //get layoutParams
        layoutParams = new WindowManager.LayoutParams();
        //set layoutParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //set width, height, x, y of layoutParams
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.x = 0;
        layoutParams.y = 0;

//        layoutParams.type=WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        layoutParams.gravity=Gravity.TOP;
//        layoutParams.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
//                // 这是为了使通知能够接收触摸事件
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                // 绘制状态栏
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.height=(int) (50 * getResources().getDisplayMetrics().scaledDensity);
//        layoutParams.format=PixelFormat.TRANSPARENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isCanceled) {
            int work=0,rest=0,sleep=0;
            if(intent!=null) {
                work = intent.getIntExtra("work", 0);
                rest = intent.getIntExtra("rest", 0);
                sleep=intent.getIntExtra("sleep",0);
            }
            System.out.println("work:" + work);
            System.out.println("rest:" + rest);
            if (work != 0) showFloatingWindow(work, rest,sleep);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingWindowPrepare() {
        if (Settings.canDrawOverlays(this)) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            //display screenSaver view
            displayView = layoutInflater.inflate(R.layout.screen_saver_activity, null);
//            displayView = layoutInflater.inflate(R.layout.image_display, null);
//            displayView.setOnTouchListener(new FloatingOnTouchListener());
//            ImageView imageView = displayView.findViewById(R.id.image_display_imageview);
//            imageView.setImageResource(images[imageIndex]);

//            View viewbar = new customViewGroup(this);
//            windowManager.addView(viewbar, layoutParams);

            //white list button
            Button whitelist = displayView.findViewById(R.id.white_list_button);
            whitelist.setOnClickListener(v -> {
                windowManager.removeView(displayView);
                System.out.println("FloatingImageDisplayService: go to whitelist");
                Intent intent = new Intent(getApplicationContext(), WhiteListActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fromScreenSaver", 1);
                startActivity(intent);
            });

            if (windowManager == null) {
                //something cannot understand happened
                System.out.println("wm is null?");
                onCreate();
            }

            System.out.println("preparation of floatingWindow has been ok");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showFloatingWindow(int workTime, int restTime,int sleep) {
        showFloatingWindowPrepare();

        Handler workHandler = new Handler(), restHanlder = new Handler(), monitorHandler=new Handler();
        workTimer = new Timer();
        restTimer = new Timer();
        monitorTimer=new Timer();

        workTimerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                System.out.println("start floating window");
                workHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isInRest=false;
                            windowManager.addView(displayView, layoutParams);
                        } catch (Exception e) {
                            System.out.println("already add view");
                        }
                    }
                });
            }
        };

        restTimerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("remove floating window");
                restHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (restTime != 0) {
                            try {
                                isInRest=true;
                                windowManager.removeView(displayView);
                            } catch (Exception e) {
                                System.out.println("it's in rest or whitelist\n" + e.toString());
                            }
                        } else
                            System.out.println("no rest");
                    }
                });
            }
        };

        monitorTimerTask = new TimerTask() {
            @Override
            public void run() {
                monitorHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String currentApp = ApkTool.getTaskPackname(getApplication());
//                        System.out.println("Current Runnning: " + currentApp);
                        if (currentApp.equals("CurrentNULL"))
                            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(FLAG_ACTIVITY_NEW_TASK));
                        if (!isAppInWhitelist(currentApp) && !currentApp.equals("com.example.wowtime")&&!isInRest)
                            try {
                                System.out.println("not in whiteList, go back.");
                                windowManager.addView(displayView, layoutParams);
                            } catch (Exception e) {
                                System.out.println("already add view");
                            }
                    }
                });
            }
        };

        //implement going time
        timingTimer = new Timer();
        timingHandler = new Handler(new Handler.Callback() {
            TextView textView;

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        ++time;
                        if (textView == null && displayView != null)
                            textView = displayView.findViewById(R.id.saver_gone_time);
                        if (textView != null)
                            textView.setText(secondToTime(time));
                }
                return false;
            }
        });
        timingTask = new TimerTask() {
            @Override
            public void run() {
                Message message = timingHandler.obtainMessage(1);
                timingHandler.sendMessage(message);
            }
        };

        if(sleep==0) {
            workTimer.schedule(workTimerTask, 0, restTime + workTime);
            restTimer.schedule(restTimerTask, workTime, workTime + restTime);
            timingTimer.schedule(timingTask, 0, 1000);
            monitorTimer.schedule(monitorTimerTask, 1000, 1000);
            System.out.println("begin 3 timers in floatingDisplayService");
        }
        else{
            workTimer.schedule(workTimerTask, 0,  workTime);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(workTime-60*1000);
                    } catch (InterruptedException e) {
                        System.out.println("interrupted when sleep alarm");
                    }
                    onDestroy();
                }
            }.start();
        }
    }

    private boolean isAppInWhitelist(String app) {
        for (String s : whitelist) {
            if (s.equals(app))
                return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isStarted = false;

        timingTimer.cancel();
        timingTask.cancel();
        workTimer.cancel();
        workTimerTask.cancel();
        restTimer.cancel();
        restTimerTask.cancel();
        monitorTimer.cancel();
        monitorTimerTask.cancel();

        try {
            windowManager.removeView(displayView);
        } catch (Exception e) {
            System.out.println("it's in rest or whitelist\n" + e.toString());
        }

//        Credit credit=new Credit();
//        credit.modifyCredit(100,"away from phone");
//        Accumulation accumulation=new Accumulation(getApplicationContext());
//        accumulation.addAccumulation(100);
//        System.out.println("accumulation: "+accumulation.getAccumulation());
        System.out.println("onDestroy finish");
    }

    public static int setIsCanceled(boolean flag) {
        isCanceled = flag;
        return time;
    }

    public static void setTime(int time) {
        FloatingImageDisplayService.time = time;
    }


    public static String secondToTime(long second) {
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;                //剩余秒数
        return hours + ":" + minutes + ":" + second;
    }

}
