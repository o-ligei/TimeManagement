package com.example.wowtime.ui.statistic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RadioButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.StatisticDayItem;
import com.example.wowtime.dto.StatisticSimple;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StatisticYearActivity extends AppCompatActivity {

    private BarChart bar;
    SharedPreferences pomodoroSp;

    List<BarEntry> list = new ArrayList<>();//实例化一个List用来存储数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_year_activity);
        bar = (BarChart) findViewById(R.id.yearBar);
        pomodoroSp = super.getSharedPreferences("pomodoro", MODE_PRIVATE);
        //添加数据
//        list.add(new BarEntry(1, 49.1f));
//        list.add(new BarEntry(12, 0));
        List<StatisticDayItem> statisticDayItems;
        String s = pomodoroSp.getString("statistic", "");
        if (s.equals(""))
            statisticDayItems = new LinkedList<>();
        else
            statisticDayItems = JSONObject.parseArray(s, StatisticDayItem.class);

        List<Float> yOfMonth=new LinkedList<>();

        for(int i=0;i<12;++i){
            yOfMonth.add((float) 0);
        }

        Calendar calendar=Calendar.getInstance();
        for(StatisticDayItem item :statisticDayItems){
            calendar.setTime(item.getBegin());
            int index=calendar.get(Calendar.MONTH);
            float oldy=yOfMonth.get(index);
            float deltay=item.getHour()+(float)item.getMinute()/60;
            float newy=oldy+deltay;
            BigDecimal bg = new BigDecimal(newy);
            yOfMonth.set(index,(float) bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }


        int max = 0;
        for (int i = 0; i < 12; ++i) {
            float tmp = yOfMonth.get(i);
            list.add(new BarEntry(i + 1, tmp));
            if (tmp > max) { max = (int) tmp; }
        }
        if (max == 0) { max = 10; }

        BarDataSet barDataSet = new BarDataSet(list, "label?");
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        bar.setData(barData);

        bar.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        bar.getAxisLeft().setDrawGridLines(true);  //是否绘制Y轴上的网格线（背景里面的横线）

        bar.getDescription().setEnabled(false);                  //是否显示右下角描述
        bar.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        bar.getDescription().setTextSize(20);                    //字体大小
        bar.getDescription().setTextColor(Color.WHITE);             //字体颜色

        bar.getAxisRight().setEnabled(false);//隐藏右侧Y轴   默认是左右两侧都有Y轴

        //图例
        Legend legend = bar.getLegend();
        legend.setEnabled(false);    //是否显示图例
//        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);    //图例的位置

        //X
        XAxis xAxis = bar.getXAxis();

        xAxis.setTextSize(14);
        xAxis.setAxisLineColor(ColorTemplate.JOYFUL_COLORS[0]);   //X轴颜色
        xAxis.setAxisLineWidth(1);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为上面
//        xAxis.setValueFormatter(new IAxisValueFormatter() {   //X轴自定义坐标
//            @Override
//            public String getFormattedValue(float v, AxisBase axisBase) {
//                return "第"+v+"天";
//            }
//        });
        xAxis.setAxisMaximum(12);   //X轴最大数值
        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(12, false);

        //Y
        YAxis AxisLeft = bar.getAxisLeft();
        AxisLeft.setTextSize(14);
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        AxisLeft.setAxisLineColor(ColorTemplate.JOYFUL_COLORS[1]);  //Y轴颜色
        AxisLeft.setAxisLineWidth(1);           //Y轴粗细
//        AxisLeft.setValueFormatter(new IAxisValueFormatter() {  //Y轴自定义坐标
//            @Override
//            public String getFormattedValue(float v, AxisBase axisBase) {
//                for (int a=0;a<16;a++){     //用个for循环方便
//                    if (a==v){
//                        return "第"+a+"个";
//                    }
//                }
//                return "";
//            }
//        });
        System.out.println("statistic year max:" + max);
        AxisLeft.setAxisMaximum(max - max % 100 + (max % 100 == 0 ? 0 : 100));   //Y轴最大数值
        AxisLeft.setAxisMinimum(0);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致y轴坐标显示不全等问题
        AxisLeft.setLabelCount(10, false);

        //柱子
//        barDataSet.setColor(Color.BLACK);  //柱子的颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) { colors.add(c); }
        for (int c : ColorTemplate.PASTEL_COLORS) { colors.add(c); }
        for (int c : ColorTemplate.COLORFUL_COLORS) { colors.add(c); }
        for (int c : ColorTemplate.LIBERTY_COLORS) { colors.add(c); }
        for (int c : ColorTemplate.JOYFUL_COLORS) { colors.add(c); }
        barDataSet.setColors(colors);//设置柱子多种颜色  循环使用
        barDataSet.setBarBorderColor(Color.WHITE);//柱子边框颜色
        barDataSet.setBarBorderWidth(1);       //柱子边框厚度
//        barDataSet.setBarShadowColor(Color.);
        barDataSet.setHighlightEnabled(true);//选中柱子是否高亮显示  默认为true
        barDataSet.setHighLightAlpha(20);
        barDataSet.setValueTextSize(14f);
//        barDataSet.setStackLabels(new String[]{"aaa","bbb","ccc"});//?
        //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i,
                    ViewPortHandler viewPortHandler) {
                if (entry.getY() == v) {
                    return (int) v + "h";
                }
                return "";
            }
        });

        bar.setFitBars(true); //使x轴完全适合所有条形
        //数据更新
        bar.notifyDataSetChanged();
        bar.invalidate();

        //动画（如果使用了动画可以则省去更新数据的那一步）
//        bar.animateY(3000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        bar.animateX(2000); //X轴动画
        bar.animateXY(1500, 1500);//XY两轴混合动画

        RadioButton radioButtonDay = findViewById(R.id.year_day);
        radioButtonDay.setOnClickListener(v -> {
//            startActivity((new Intent
//                (StatisticYearActivity.this, StatisticDayActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            startActivity(new Intent(StatisticYearActivity.this, StatisticDayActivity.class));
            finish();
        });
        RadioButton radioButtonWeek = findViewById(R.id.year_week);
        radioButtonWeek.setOnClickListener(v -> {
//            startActivity((new Intent
//                    (StatisticYearActivity.this, StatisticWeekActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            startActivity(new Intent(StatisticYearActivity.this, StatisticWeekActivity.class));
            finish();
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // should Auto-generated method stub but it seems there is nothing
        super.onNewIntent(intent);
        //退出
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        return super.onCreateOptionsMenu(menu);
    }
}

