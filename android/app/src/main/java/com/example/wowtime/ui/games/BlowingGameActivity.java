package com.example.wowtime.ui.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.util.AudioRecordManger;

import java.sql.Timestamp;
import java.util.Date;

public class BlowingGameActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private ProgressBar progressBar;
    private TextView progressValue;
//    private LinearLayout full;

//    private int x1, x2, dx;
    private int blowCount = 0;
    private Long t1 = System.currentTimeMillis(), t2;

    private static final int RECORD = 2;              //监听话筒
    private int VOLUME_THRESHOLD = 60;
    private int BLOW_INTERVAL = 250;
    private int TOTAL_TIME = 5000;
    private int NEED_COUNT = TOTAL_TIME/BLOW_INTERVAL;

    private void handleSoundValues(double values){
        if (blowCount == NEED_COUNT) {
            mp.stop();
            BlowingGameActivity.this.finish();
        }
        if (values > VOLUME_THRESHOLD){
            t2 = System.currentTimeMillis();
            if (t2-t1>=BLOW_INTERVAL) {
                t1 = t2;
                blowCount++;
                progressBar.setProgress(100*blowCount/NEED_COUNT);
                progressValue.setText(new StringBuffer().append(progressBar.getProgress()).append("%"));
                System.out.println(blowCount);
            }
        }
    }

    private Handler handler = new Handler(msg -> {
        if (msg.what == RECORD) {//监测话筒
            double soundValues = (double) msg.obj;
            handleSoundValues(soundValues);
        }
        return false;
    });

    private void paramSetting() {
        SharedPreferences blowSettingPreference =
                PreferenceManager.getDefaultSharedPreferences(this);
        VOLUME_THRESHOLD = Integer.parseInt(blowSettingPreference.getString("blow_volume", "60"));
        TOTAL_TIME = Integer.parseInt(blowSettingPreference.getString("blow_duration", "5000"));
        System.out.println("volume: "+VOLUME_THRESHOLD);
        System.out.println("duration: "+TOTAL_TIME);
        NEED_COUNT = TOTAL_TIME/BLOW_INTERVAL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blowing_game_activity);
        progressBar = findViewById(R.id.progress1);
        progressValue = findViewById(R.id.progress_value1);
//        full = findViewById(R.id.full);
//        initView();
        paramSetting();
        //调用话筒实现类
        AudioRecordManger audioRecordManger = new AudioRecordManger(handler, RECORD); //实例化话筒实现类
        audioRecordManger.getNoiseLevel();                         //打开话筒监听声音
        mp = MediaPlayer.create(this, R.raw.radar);
        mp.setLooping(true);
        mp.start();
    }

//    @SuppressLint("ClickableViewAccessibility")
//    private void initView() {
//        //外面的父view设置触摸监听事件
//        full.setOnTouchListener((v, event) -> {
//            int w = getWindowManager().getDefaultDisplay().getWidth();
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    x1 = (int) event.getRawX();
//                    progressBar.setProgress(100 * x1 / w);
//                    setPos();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    x2 = (int) event.getRawX();
//                    dx = x2 - x1;
//                    if (Math.abs(dx) > w / 100) { //改变条件 调整进度改变速度
//                        x1 = x2; // 去掉已经用掉的距离， 去掉这句 运行看看会出现效果
//                        progressBar.setProgress(progressBar.getProgress() + dx * 100 / w);
//                        setPos();
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//            }
//            return true;
//        });
//    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            setPos();
//        }
//    }

    /**
     * 设置进度显示在对应的位置
     */
//    public void setPos() {
//        int w = getWindowManager().getDefaultDisplay().getWidth();
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) progressValue.getLayoutParams();
//        int pro = progressBar.getProgress();
//        int tW = progressValue.getWidth();
//        if ((double) w * pro / 100 + tW * 0.3 > w) {
//            params.leftMargin = (int) (w - tW * 1.1);
//        } else if ((double) w * pro / 100 < tW * 0.7) {
//            params.leftMargin = 0;
//        } else {
//            params.leftMargin = (int) ((double) w * pro / 100 - tW * 0.7);
//        }
//        progressValue.setLayoutParams(params);
//        progressValue.setText(new StringBuffer().append(progressBar.getProgress()).append("%"));
//    }
}