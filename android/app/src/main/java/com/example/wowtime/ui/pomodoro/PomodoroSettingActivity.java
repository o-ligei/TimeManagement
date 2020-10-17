package com.example.wowtime.ui.pomodoro;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;

import java.util.ArrayList;
import java.util.List;


public class PomodoroSettingActivity extends AppCompatActivity {
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
}
