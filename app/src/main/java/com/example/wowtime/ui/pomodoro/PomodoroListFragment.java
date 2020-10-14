package com.example.wowtime.ui.pomodoro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.PomodoroItemAdapter;
import com.example.wowtime.dto.PomodoroListItem;

import java.util.ArrayList;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pomodoro_list_fragment, container, false);
        ArrayList<PomodoroListItem> list = new ArrayList<>();
        list.add(new PomodoroListItem("ICS 强模式 不休息","30min"));
        list.add(new PomodoroListItem("CSE 弱模式 每30min休息5min","60min"));

        PomodoroItemAdapter adapter = new PomodoroItemAdapter(list,getContext());
        ListView listView = view.findViewById(R.id.PomodoroCardList);
        listView.setAdapter(adapter);

        return view;
    }
}