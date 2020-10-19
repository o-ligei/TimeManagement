package com.example.wowtime.ui.pomodoro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;

import java.util.ArrayList;
import java.util.List;


public class PomodoroSettingActivity extends AppCompatActivity {

//    private FloatView mFloatView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro_setting_activity);

        TimePicker timePicker=findViewById(R.id.PomodorotimePicker);
        timePicker.setIs24HourView(true);

        TimePicker timePicker2=findViewById(R.id.PomodorotimePicker2);
        timePicker2.setIs24HourView(true);

        TimePicker timePicker3=findViewById(R.id.PomodorotimePicker3);
        timePicker3.setIs24HourView(true);

        resizePikcer(timePicker2);
        resizePikcer(timePicker3);

        timePicker.setHour(0);
        timePicker.setMinute(30);
        timePicker2.setHour(0);
        timePicker2.setMinute(30);
        timePicker3.setHour(0);
        timePicker3.setMinute(10);

        TextView textView=findViewById(R.id.PomodoroSelectWhiteListText);
        textView.setOnClickListener(v->startActivity(new Intent(PomodoroSettingActivity.this,WhiteListActivity.class)));
//        getAppList();

        Button button=findViewById(R.id.setPomodoroButton);
        button.setOnClickListener(v->{
            int minute=timePicker.getMinute();
            int hour=timePicker.getHour();
            int totalTime=minute*60*1000+hour*3600*1000;
            int minute2=timePicker2.getMinute();
            int hour2=timePicker2.getHour();
            int time=minute2*60*1000+hour2*3600*1000;
            int minute3=timePicker3.getMinute();
            int hour3=timePicker3.getHour();
            int rest=minute3*60*1000+hour3*3600*1000;

            int count=totalTime/(time+rest);

//            for(int i=0;i<count;++i){
                startFloatingImageDisplayService(button);
                Thread t=new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(30*1000);
                            System.out.println(30);
                        } catch (InterruptedException e) {
                            System.out.println("interrupt when screen saver");
                            e.printStackTrace();
                        }
                        stopFloatingImageDisplayService(button);
                        try {
                            sleep(10*1000);
                            System.out.println(10);
                        } catch (InterruptedException e) {
                            System.out.println("interrupt when rest");
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
//                try {
//                    System.out.println("join");
//                    t.join();
//                } catch (InterruptedException e) {
//                    System.out.println("interrupt when task");
//                    e.printStackTrace();
//                }
//            }

        });
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

    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, FrameLayout.LayoutParams.WRAP_CONTENT);
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
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1);
        } else {
            System.out.println("Floating window starts!" );
            startService(new Intent(PomodoroSettingActivity.this, FloatingImageDisplayService.class));
        }
    }

    public void stopFloatingImageDisplayService(View view) {
        if (!FloatingImageDisplayService.isStarted) {
            System.out.println("Floating window is already stoppped!" );
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
}
