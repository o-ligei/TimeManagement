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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.wowtime.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dongzhong on 2018/5/30.
 */

public class FloatingImageDisplayService extends Service {
    public static boolean isStarted = false;
    private static boolean isCanceled=false;
    private static int time=0;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;

    private Timer timingTimer;
    private TimerTask timingTask;
    private static Handler timingHandler;

//    private int[] images;
//    private int imageIndex = 0;
//
//    private Handler changeImageHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        if(isCanceled) {
            System.out.println("canceled");
            return;
        }
        System.out.println("floatingService in creating");
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

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


//        changeImageHandler = new Handler(this.getMainLooper(), changeImageCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isCanceled)
            showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView=layoutInflater.inflate(R.layout.screen_saver_activity,null);
//            displayView = layoutInflater.inflate(R.layout.image_display, null);
//            displayView.setOnTouchListener(new FloatingOnTouchListener());
//            ImageView imageView = displayView.findViewById(R.id.image_display_imageview);
//            imageView.setImageResource(images[imageIndex]);

//            View viewbar = new customViewGroup(this);
//            windowManager.addView(viewbar, layoutParams);

            TextView textView=displayView.findViewById(R.id.saver_gone_time);
            timingTimer = new Timer();
            timingTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = timingHandler.obtainMessage(1);
                    timingHandler.sendMessage(message);
//                    System.out.println("send");
                }
            };
            Button whitelist=displayView.findViewById(R.id.white_list_button);
            whitelist.setOnClickListener(v->{
                windowManager.removeView(displayView);
                System.out.println("FloatingImageDisplayService:whitelist");
                Intent intent=new Intent(getApplicationContext(),WhiteListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                intent.putExtra("fromScreenSaver",1);
               startActivity(intent);
            });
            timingHandler=new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            ++time;
//                            System.out.println("time has gone"+time+"s");
                            textView.setText(secondToTime(time));
                            break;
                    }
                    return false;
                }
            });
            if(windowManager==null) {
                System.out.println("wm is null?");
                onCreate();
            }
            windowManager.addView(displayView, layoutParams);
            timingTimer.schedule(timingTask, 0, 1000);
//            changeImageHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStarted=false;
        timingTimer.cancel();
        timingTask.cancel();
        System.out.println("destroy");
        windowManager.removeView(displayView);
    }

    public static int setIsCanceled(boolean flag) {
        isCanceled=flag;
        return time;
    }

    public static void setTime(int time) {
        FloatingImageDisplayService.time = time;
    }

    public static String secondToTime(long second){
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数
        return hours + ":" + minutes + ":" + second;
    }


//    private class FloatingOnTouchListener implements View.OnTouchListener {
//        private int x;
//        private int y;
//
//        @Override
//        public boolean onTouch(View view, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    x = (int) event.getRawX();
//                    y = (int) event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    int nowX = (int) event.getRawX();
//                    int nowY = (int) event.getRawY();
//                    int movedX = nowX - x;
//                    int movedY = nowY - y;
//                    x = nowX;
//                    y = nowY;
//                    layoutParams.x = layoutParams.x + movedX;
//                    layoutParams.y = layoutParams.y + movedY;
//                    windowManager.updateViewLayout(view, layoutParams);
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        }
//    }
}
