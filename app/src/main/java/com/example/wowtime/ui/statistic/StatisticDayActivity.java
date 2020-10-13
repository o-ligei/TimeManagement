package com.example.wowtime.ui.statistic;


import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;

import android.widget.RadioButton;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticDayActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_statistic);
//        PieChartView pieChartView = (PieChartView) findViewById(R.id.chart);
//
//        List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();
//
//        pieceDataHolders.add(new PieChartView.PieceDataHolder(1000, 0xFF8FDCD9, "预习ICS\n15:00-15:25"));
//        pieceDataHolders.add(new PieChartView.PieceDataHolder(800, 0xFFCB72D8, "洗衣服\n15:30-15:45"));
//        pieceDataHolders.add(new PieChartView.PieceDataHolder(4000, 0xFFFF9800, "App原型UI\n21:00-23:00"));
//        pieceDataHolders.add(new PieChartView.PieceDataHolder(5000, 0xFFFFB6C1, "写选修课论文\n19:00-20:20"));
//        pieceDataHolders.add(new PieChartView.PieceDataHolder(3000, 0xFF9DDC98, "写CSE-lab1\n12:30-14:00"));
//
//        pieChartView.setData(pieceDataHolders);
//    }
    private PieChart pie;
    List<PieEntry> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        pie = (PieChart) findViewById(R.id.chart);

        list=new ArrayList<>();
        list.add(new PieEntry(10,"预习ICS","9:00-9:45"));
        list.add(new PieEntry(12,"洗衣服","12:40-13:30"));
        list.add(new PieEntry(21,"App原型","14:00-15:30"));
        list.add(new PieEntry(27,"CSE-lab1","19:00-21:00"));
        list.add(new PieEntry(30,"Android","21:30-23:00"));

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

        pie.setBackgroundColor(0xFFFFCCBC);
        pie.setNoDataText(String.valueOf(R.string.statistic_no_data));//设置当chart为空时显示的描述文字。
        pie.setUsePercentValues(true);//使用百分比显示
        pie.setExtraOffsets(15, 0, 15, 0);//设置图表上下左右的偏移，类似于外边距
        pie.setDragDecelerationFrictionCoef(0.95f);
        //实体扇形的空心圆的半径   设置成0时就是一个圆 而不是一个环
        pie.setHoleRadius(50f);
        //中间半透明白色圆的半径    设置成0时就是隐藏
        pie.setTransparentCircleRadius(53f);
        //设置中心圆的颜色
        pie.setHoleColor(0xFFFFCCBC);
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
                int totalSectors=pieDataSet.getEntryCount();
                for(int i=0;i<totalSectors;++i){
                    if(pieDataSet.getEntryForIndex(i).getY()==e.getY()){//pieDataSet.getEntryForIndex(i).getX()==e.getX()&&
                        TextView textView=findViewById(R.id.textViewDayBottom);
                        textView.setText(pieDataSet.getEntryForIndex(i).getLabel()+"\n"+pieDataSet.getEntryForIndex(i).getData());
                        System.out.println("pie click:"+pieDataSet.getEntryForIndex(i).getLabel());
                    }
                }
            }

            @Override
            public void onNothingSelected() {
                TextView textView=findViewById(R.id.textViewDayBottom);
                textView.setText("");
            }
        });


        RadioButton radioButtonWeek=findViewById(R.id.day_week);
        radioButtonWeek.setOnClickListener(v->startActivity(new Intent(StatisticDayActivity.this, StatisticWeekActivity.class)));
        RadioButton radioButtonYear=findViewById(R.id.day_year);
        radioButtonYear.setOnClickListener(v->startActivity(new Intent(StatisticDayActivity.this, StatisticYearActivity.class)));

    }
}
