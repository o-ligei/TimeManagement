package com.example.wowtime.ui.pomodoro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

import com.example.wowtime.R;

import java.util.Timer;
import java.util.TimerTask;

public class FloatingImageDisplayService extends Service {
    public static boolean isStarted = false;
    private static boolean isCanceled = false;    //when canceled, cannot be start again
    private static int time = 0;  // notice that it's statistic

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;

    private Timer timingTimer, workTimer, restTimer;
    private TimerTask timingTask, workTimerTask, restTimerTask;
    private static Handler timingHandler;


    @Override
    public void onCreate() {
        super.onCreate();

        if (isCanceled) {
            System.out.println("canceled");
            return;
        }

        isStarted = true;
        System.out.println("floatingService is creating");
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
            int work = intent.getIntExtra("work", 0);
            int rest = intent.getIntExtra("rest", 0);
            System.out.println("work:" + work);
            System.out.println("rest:" + rest);
            if (work != 0) showFloatingWindow(work, rest);
            else showFloatingWindow();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingWindow() {
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fromScreenSaver", 1);
                startActivity(intent);
            });

            if (windowManager == null) {
                //something cannot understand happened
                System.out.println("wm is null?");
                onCreate();
            }

            //display and begin timing
//            windowManager.addView(displayView, layoutParams);
            System.out.println("preparation of floatingWindow has been ok");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showFloatingWindow(int workTime, int restTime) {
        showFloatingWindow();

        Handler workHandler = new Handler(), restHanlder = new Handler();
        workTimer = new Timer();
        restTimer = new Timer();

        workTimerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                System.out.println("start floating window");
                workHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        windowManager.addView(displayView, layoutParams);
//                        System.out.println("stop");
//                        int SCAN_INTERVAL = 1000;
//                        for (int i = 0; i < rest / SCAN_INTERVAL - 2; i++) {
//                            String currentApp = ApkTool.getTaskPackname(PomodoroSettingActivity.this);
//                            System.out.println("Current Runnning: " + currentApp);
//                            if (currentApp.equals("CurrentNULL"))
//                                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//                            if (currentApp.equals("com.netease.cloudmusic"))
//                                runOnUiThread(() -> {
//                                    startFloatingImageDisplayService(buttonBegin);
//                                });
//                            try {
//                                Thread.sleep(SCAN_INTERVAL);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
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
                        windowManager.removeView(displayView);
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

        workTimer.schedule(workTimerTask, 0, restTime + workTime);
        restTimer.schedule(restTimerTask, workTime, workTime + restTime);
        timingTimer.schedule(timingTask, 0, 1000);
        System.out.println("begin 3 timers in floatingDisplayService");
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

        try {
            windowManager.removeView(displayView);
        } catch (Exception e) {
            System.out.println("it's in rest\n" + e.toString());
        }

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
