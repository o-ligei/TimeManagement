package com.example.wowtime.ui.pomodoro;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.PomodoroListItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class PomodoroSettingActivity extends AppCompatActivity {

//    private FloatView mFloatView;
    private SharedPreferences pomodoroSp;
    int mode=0;

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

        Spinner spinner=findViewById(R.id.PomodoroModeSpinner);
        TextView spinnerText=findViewById(R.id.PomodoroSelectModeText);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//通过此方法为下拉列表设置点击事件
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode=i;
                String text= spinner.getItemAtPosition(i).toString();
                spinnerText.setText(text);
                System.out.println("PomodoroMode:"+i);
//                Toast.makeText(PomodoroSettingActivity.this,text,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerText.setText( (String) getBaseContext().getResources().getText(R.string.pomodoro_mode));
            }
        });

        EditText editText=findViewById(R.id.editName);

        resizePikcer(timePicker2);
        resizePikcer(timePicker3);

        timePicker.setHour(1);
        timePicker.setMinute(10);
        timePicker2.setHour(0);
        timePicker2.setMinute(30);
        timePicker3.setHour(0);
        timePicker3.setMinute(5);

        TextView textView=findViewById(R.id.PomodoroSelectWhiteListText);
        textView.setOnClickListener(v->startActivity(new Intent(PomodoroSettingActivity.this,WhiteListActivity.class)));
//        getAppList();

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

            int count=totalTime/(time+rest);

//            for(int i=0;i<count;++i){
                startFloatingImageDisplayService(buttonBegin);
                new Thread(){
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
                        stopFloatingImageDisplayService(buttonBegin);
                        try {
                            sleep(10*1000);
                            System.out.println(10);
                        } catch (InterruptedException e) {
                            System.out.println("interrupt when rest");
                            e.printStackTrace();
                        }
                    }
                }.start();

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    PackageManager packageManager=PomodoroSettingActivity.this.getPackageManager();
                    List<PackageInfo> packageInfos=ApkTool.scanLocalInstallAppListByPackage(packageManager);
                    int i=0;
                    System.out.println("begin to watching running apps");
                    while (++i<=10) {
                        for (PackageInfo packageInfo : packageInfos) {
                            if (isRunning(getApplicationContext(), packageInfo.packageName)) {
                                System.out.println("runningApp:" + packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                            }
                        }

                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }.start();
//                try {
//                    System.out.println("join");
//                    t.join();
//                } catch (InterruptedException e) {
//                    System.out.println("interrupt when task");
//                    e.printStackTrace();
//                }
//            }

        });

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
            List<PomodoroListItem> pomodoroListItems=  JSON.parseArray(stringList,PomodoroListItem.class);
            if(pomodoroListItems==null)
                pomodoroListItems=new LinkedList<>();
            pomodoroListItems.add(pomodoroListItem);
            editor.putString("pomodoroList",JSONObject.toJSONString(pomodoroListItems));
            editor.commit();
            Toast.makeText(this,(String) getBaseContext().getResources().getText(R.string.pomodoro_save_successfully),Toast.LENGTH_LONG).show();
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

}
