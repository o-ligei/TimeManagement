package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.wowtime.util.AudioRecordManger;

public class BlowingGame extends AppCompatActivity {
    private static final int RECORD = 2;              //监听话筒

    private void getSoundValues(double values){
        //话筒分贝大于50
        if (values >50){
            Toast.makeText(BlowingGame.this, "你在吹气哦", Toast.LENGTH_SHORT).show();
        }
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == RECORD) {//监测话筒
                double soundValues = (double) msg.obj;
                getSoundValues(soundValues);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blowing_game);
        //调用话筒实现类
        AudioRecordManger audioRecordManger = new AudioRecordManger(handler, RECORD); //实例化话筒实现类
        audioRecordManger.getNoiseLevel();                         //打开话筒监听声音
    }
}