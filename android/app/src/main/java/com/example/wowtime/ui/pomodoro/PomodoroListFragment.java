package com.example.wowtime.ui.pomodoro;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.wowtime.R;
import com.example.wowtime.adapter.PomodoroItemAdapter;
import com.example.wowtime.adapter.PomodoroItemAdapter;
import com.example.wowtime.dto.PomodoroListItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PomodoroListFragment extends Fragment {


    public PomodoroListFragment() {}

    public PomodoroListFragment(int contentLayoutId) {
        super(contentLayoutId);
    }


    private SharedPreferences pomodoroSp;
    private PomodoroItemAdapter adapter;
    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //pomodoro.xml
        pomodoroSp=super.getActivity().getSharedPreferences("pomodoro",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pomodoro_list_fragment, container, false);

        String stringList=pomodoroSp.getString("pomodoroList","");
        System.out.println("pomodoroList:"+stringList);
        List<PomodoroListItem> listItems = new LinkedList<>();
        if (stringList != null && !stringList.equals(""))
            listItems = JSON.parseArray(stringList, PomodoroListItem.class);
        adapter = new PomodoroItemAdapter(listItems,getContext());
        listView = view.findViewById(R.id.PomodoroCardList);

        listView.setAdapter(adapter);
        adapter.setOnItemClickListener((view1, position) -> {
            System.out.println("pomodoroPosition:"+position);
        });
/*
//        View view2 = inflater.inflate(R.layout.activity_main, container, false);
//        ListView listView2=view2.findViewById(R.id.PomodoroCardList);
//        if(listView2==null)System.out.println("!!!");
//        else
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                List<> urls=new ArrayList<>();
//
//                //获取当前选择的值
//                Adapter adpter=parent.getAdapter();
//                for (int i=0;i<adpter.getCount();i++){
//                    ImageItem item=(ImageItem)adpter.getItem(i);//拿到当前数据值并强转   adpter.getItem(i)即为当前数据对象
//                    String data=item.getNetUrl();
//                    urls.add(data);
//                }
                System.out.println("???"+position);

            }
        });
*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String stringList=pomodoroSp.getString("pomodoroList","");
        System.out.println("pomodoroList:"+stringList);
        List<PomodoroListItem> listItems = new LinkedList<>();
        if (stringList != null && !stringList.equals(""))
            listItems = JSON.parseArray(stringList, PomodoroListItem.class);
        adapter = new PomodoroItemAdapter(listItems,getContext());
        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }
}