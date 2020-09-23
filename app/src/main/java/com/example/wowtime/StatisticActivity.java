package com.example.wowtime;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        PieChartView pieChartView = (PieChartView) findViewById(R.id.pie_chart);

        List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();

        pieceDataHolders.add(new PieChartView.PieceDataHolder(1000, 0xFF8FDCD9, "预习ICS\n15:00-15:25"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(800, 0xFFCB72D8, "洗衣服\n15:30-15:45"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(4000, 0xFFFF9800, "App原型UI\n21:00-23:00"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(5000, 0xFFFFB6C1, "写选修课论文\n19:00-20:20"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(3000, 0xFF9DDC98, "写CSE-lab1\n12:30-14:00"));

        pieChartView.setData(pieceDataHolders);
    }
}
