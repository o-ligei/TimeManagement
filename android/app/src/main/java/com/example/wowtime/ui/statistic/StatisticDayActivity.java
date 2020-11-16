package com.example.wowtime.ui.statistic;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.StatisticDayItem;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StatisticDayActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.statistic_day_activity);
//        PieChartView pieChartView = (PieChartView) findViewById(R.id.chart);
//
//        List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();
//
//        pieceDataHolders.add(new PieChartView.PieceDataHolder(1000, 0xFF8FDCD9, "预习ICS\n15:00-15:25"));
//
//        pieChartView.setData(pieceDataHolders);
//    }
    private PieChart pie;
    List<PieEntry> list;
    private SharedPreferences pomodoroSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_day_activity);
        pie = (PieChart) findViewById(R.id.chart);
        pomodoroSp=super.getSharedPreferences("pomodoro",MODE_PRIVATE);

        list=new ArrayList<>();
//        list.add(new PieEntry(10,"预习ICS","9:00-9:45"));
        String statisticString=pomodoroSp.getString("statistic","");
        List<StatisticDayItem> statisticDayItems= JSON.parseArray(statisticString,StatisticDayItem.class);
        List<Integer> removedIndex=new LinkedList<>();
        Date now=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for(int i=0;i<statisticDayItems.size();++i){
            StatisticDayItem item=statisticDayItems.get(i);
            Date begin=item.getBegin();
            if(begin.getYear()!=now.getYear()){
                removedIndex.add(i);
                continue;
            }
            if(begin.getDay()==now.getDay()||begin.getYear()==now.getYear()||begin.getMonth()==now.getMonth()){
                System.out.println("StatisticDay: today"+item.getName());
                list.add(new PieEntry(item.getMinute()+item.getHour(),item.getName(),
                        sdf.format(begin)+"-"+sdf.format(item.getEnd())));
            }
        }

        for(int index:removedIndex)
            statisticDayItems.remove(index);
        SharedPreferences.Editor editor=pomodoroSp.edit();
        editor.putString("statistic", JSONObject.toJSONString(statisticDayItems));
        editor.apply();

//        List<StatisticWeekItem> statisticWeekItems=JSONObject.parseArray(pomodoroSp.getString("statisticWeek",""),
//                StatisticWeekItem.class);
//        for(StatisticDayItem dayItem:oldItems){
//            boolean flag=false;
//            Date date=dayItem.getBegin();
//            Calendar day=Calendar.getInstance();
//            day.setTime(date);
//            for(StatisticWeekItem weekItem:statisticWeekItems){
//                if(day==weekItem.getDay()){
//                    weekItem.setHour(weekItem.getHour()+dayItem.getHour()+dayItem.getMinute()/60);
//                    flag=true;
//                    break;
//                }
//            }
//            if(!flag){
//                statisticWeekItems.add(new StatisticWeekItem(dayItem.getHour()+dayItem.getMinute()/60,day));
//            }
//        }
//        editor.putString("statisticWeek",JSONObject.toJSONString(statisticWeekItems));


        PieDataSet pieDataSet=new PieDataSet(list,"");
        pieDataSet.setSliceSpace(3f);//设置不同DataSet之间的间距
        pieDataSet.setSelectionShift(5f);
//        pieDataSet.setHighlightEnabled(true);
        //设置各个数据的颜色
//        pieDataSet.setColors(0xFF8FDCD9,0xFFCB72D8,0xFFFF9800,0xFFFFB6C1,0xFF9DDC98);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);
        PieData pieData=new PieData(pieDataSet);
        pieData.setValueTextColor(Color.WHITE);
        pie.setData(pieData);

//        pie.setBackgroundColor(0xFFFFCCBC);
        pie.setNoDataText(String.valueOf(R.string.statistic_no_data));//设置当chart为空时显示的描述文字。
        pie.setUsePercentValues(true);//使用百分比显示
        pie.setExtraOffsets(15, 0, 15, 0);//设置图表上下左右的偏移，类似于外边距
        pie.setDragDecelerationFrictionCoef(0.95f);
        //实体扇形的空心圆的半径   设置成0时就是一个圆 而不是一个环
        pie.setHoleRadius(50f);
        //中间半透明白色圆的半径    设置成0时就是隐藏
        pie.setTransparentCircleRadius(53f);
        //设置中心圆的颜色
//        pie.setHoleColor(0xFFFFCCBC);
        pie.setTransparentCircleColor(Color.WHITE);//?
        pie.setTransparentCircleAlpha(110);//?
        //设置中心部分的字  （一般中间白色圆不隐藏的情况下才设置）
        pie.setCenterText("每日总结");
        //设置中心字的字体颜色
        pie.setCenterTextColor(Color.WHITE);
        //设置中心字的字体大小
        pie.setCenterTextSize(22);
        //设置描述的字体大小

        pie.setEntryLabelTextSize(16);
        //设置数据的字体大小  （图中的  44     56）
        pieDataSet.setValueTextSize(16);

        //设置描述的位置
//        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setValueLinePart1Length(0.2f);//设置描述连接线长度
//        //设置数据的位置
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setValueLinePart2Length(0.1f);//设置数据连接线长度
//        //设置两根连接线的颜色
//        pieDataSet.setValueLineColor(Color.WHITE);

        //对于右下角一串字母的操作
        pie.getDescription().setEnabled(false);                  //是否显示右下角描述
        pie.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示

        pie.getDescription().setTextSize(16);                    //字体大小

        pie.getDescription().setTextColor(Color.WHITE);             //字体颜色

        //图例
        Legend legend=pie.getLegend();
        legend.setEnabled(true);    //是否显示图例
//        legend.setTextColor(Color.WHITE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);   //图例的位置

        //旋转
        pie.setRotationEnabled(true);
        pie.setHighlightPerTapEnabled(true);


        //数据更新
        pie.notifyDataSetChanged();
        pie.invalidate();


        //动画（如果使用了动画可以则省去更新数据的那一步）
        pie.animateY(1000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        line.animateX(2000); //X轴动画
//        line.animateXY(2000,2000);//XY两轴混合动画

        pie.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e==null)return;
//                String s=JSONObject.toJSONString(e);
                System.out.println("StatisticDay: click"+JSONObject.toJSONString(e));
                TextView textView=findViewById(R.id.textViewDayBottom);
                PieEntry pieEntry=(PieEntry) e;
                textView.setText(pieEntry.getLabel()+"\n"+pieEntry.getData());
//                PieEntry pieEntry=JSON.parseObject(s,PieEntry.class);
//                textView.setText(pieEntry.getLabel()+"\n"+pieEntry.getData());

//                int totalSectors=pieDataSet.getEntryCount();
//                for(int i=0;i<totalSectors;++i){
//                    if(pieDataSet.getEntryForIndex(i).getX()==e.getX()&&pieDataSet.getEntryForIndex(i).getY()==e.getY()){//pieDataSet.getEntryForIndex(i).getX()==e.getX()&&
//                        TextView textView=findViewById(R.id.textViewDayBottom);
//                        textView.setText(pieDataSet.getEntryForIndex(i).getLabel()+"\n"+pieDataSet.getEntryForIndex(i).getData());
//                        System.out.println("pie click:"+pieDataSet.getEntryForIndex(i).getLabel());
//                    }
//                }
            }

            @Override
            public void onNothingSelected() {
                TextView textView=findViewById(R.id.textViewDayBottom);
                textView.setText("");
            }
        });


        RadioButton radioButtonWeek=findViewById(R.id.day_week);
        radioButtonWeek.setOnClickListener(v->{
//            startActivity((new Intent
//                (StatisticDayActivity.this, StatisticWeekActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            startActivity(new Intent(StatisticDayActivity.this, StatisticWeekActivity.class));
            finish();
        });
        RadioButton radioButtonYear=findViewById(R.id.day_year);
        radioButtonYear.setOnClickListener(v->{
//            startActivity((new Intent
//                (StatisticDayActivity.this, StatisticYearActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            startActivity(new Intent(StatisticDayActivity.this, StatisticYearActivity.class));
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
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
}
