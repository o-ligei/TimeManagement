package com.example.wowtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wowtime.adapter.AlarmItemAdapter;
import com.example.wowtime.component.AlarmListItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

//public class AlarmList extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_alarm_list);
//
//        ArrayList<AlarmListItem> test=new ArrayList<>();
//        AlarmListItem alarm1=new AlarmListItem("night","23:00");
//        AlarmListItem alarm2=new AlarmListItem("morning","06:00");
//        AlarmListItem alarm3=new AlarmListItem("weekend","08:00");
//        test.add(alarm1);
//        test.add(alarm2);
//        test.add(alarm3);
//
//        AlarmItemAdapter adapter=new AlarmItemAdapter(test,getApplicationContext());
//        ListView listView=findViewById(R.id.AlarmCardList);
//        listView.setAdapter(adapter);
//    }
//}
public class AlarmList extends Fragment{
    public AlarmList(){}

    public AlarmList(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_alarm_list, container, false);
        ArrayList<AlarmListItem> test = new ArrayList<>();
        AlarmListItem alarm1 = new AlarmListItem("ICS 不重复", "23:00");
        AlarmListItem alarm2 = new AlarmListItem("CSE 每天", "06:00");
        AlarmListItem alarm3 = new AlarmListItem("编译 周二", "08:00");
        AlarmListItem alarm4 = new AlarmListItem("ICS 不重复", "23:00");
        AlarmListItem alarm5 = new AlarmListItem("啦啦 每天", "06:00");
        AlarmListItem alarm6 = new AlarmListItem("123 周一", "08:00");
        AlarmListItem alarm7 = new AlarmListItem("ICS 不重复", "23:00");
        AlarmListItem alarm8 = new AlarmListItem("CSE 每天", "06:00");
        AlarmListItem alarm9 = new AlarmListItem("编译 周二", "08:00");
        test.add(alarm1);
        test.add(alarm2);
        test.add(alarm3);
        test.add(alarm4);
        test.add(alarm5);
        test.add(alarm6);
        test.add(alarm7);
        test.add(alarm8);
        test.add(alarm9);

        AlarmItemAdapter adapter = new AlarmItemAdapter(test, getContext());
        ListView listView = root.findViewById(R.id.AlarmCardList);
        listView.setAdapter(adapter);

        return root;
    }

}