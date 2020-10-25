package com.example.wowtime.ui.pomodoro;

import android.content.SharedPreferences;
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
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PomodoroListFragment extends Fragment {

//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;

    public PomodoroListFragment() {}

    public PomodoroListFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PomodoroListFragment.
//     */
//    public static PomodoroListFragment newInstance(String param1, String param2) {
//        PomodoroListFragment fragment = new PomodoroListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    private SharedPreferences pomodoroSp;

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
//        ArrayList<PomodoroListItem> list = new ArrayList<>();
//        list.add(new PomodoroListItem("ICS 强模式 不休息","30min"));
//        list.add(new PomodoroListItem("CSE 弱模式 每30min休息5min","60min"));

        String stringList=pomodoroSp.getString("pomodoroList","");
        System.out.println("pomodoroList:"+stringList);
        List<PomodoroListItem> listItems=  JSON.parseArray(stringList,PomodoroListItem.class);

        PomodoroItemAdapter adapter = new PomodoroItemAdapter(listItems,getContext());
        ListView listView = view.findViewById(R.id.PomodoroCardList);

        listView.setAdapter(adapter);
        adapter.setOnItemClickListener((view1, position) -> {
            System.out.println("!!!"+position);
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
}