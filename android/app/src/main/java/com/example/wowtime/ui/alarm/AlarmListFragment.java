package com.example.wowtime.ui.alarm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.adapter.AlarmItemAdapter;
import com.example.wowtime.dto.AlarmListItem;

import java.util.ArrayList;
import java.util.List;

//public class AlarmList extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.alarm_list_fragment);
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
public class AlarmListFragment extends Fragment{
    List<AlarmListItem> alarmList = new ArrayList<>();

    public AlarmListFragment(){}

    public AlarmListFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.alarm_list_fragment, container, false);
//        AlarmItemAdapter adapter = new AlarmItemAdapter(alarmList, getContext());
        ListView listView = root.findViewById(R.id.AlarmCardList);
//        listView.setAdapter(adapter);
//        System.out.println(Uri.parse("R.raw.radar"));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mySharedPreferences= requireActivity().getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
        String shared=mySharedPreferences.getString("list","");
        System.out.println("alarmList:"+shared);
        if(shared==null|| shared.equals("")){
            return;
        }
        alarmList=JSONObject.parseArray(shared,AlarmListItem.class);
        AlarmItemAdapter adapter = new AlarmItemAdapter(alarmList, getContext());
        ListView listView = requireView().findViewById(R.id.AlarmCardList);
//        String [] alarms=dto.split(";");
//        for (String alarm:alarms){
//            String [] attributes=alarm.split(",");
//            AlarmListItem alarmListItem=new AlarmListItem(attributes[0],attributes[3],attributes[1],attributes[2],Integer.parseInt(attributes[4]),Integer.parseInt(attributes[5]));
//            alarmList.add(alarmListItem);
//        }
        listView.setAdapter(adapter);

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                System.out.println("long click");
//                android.app.AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
//                dialog.setTitle("删除闹钟");//设置标题
////                dialog.setMessage("something important");//设置信息具体内容
//                dialog.setCancelable(true);//设置是否可取消
//                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override//设置ok的事件
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        System.out.println("ok");
////                        System.out.println("alarm size:"+alarmList.size());
//                        alarmList.remove(position);
////                        System.out.println("alarm size:"+alarmList.size());
//                        AlarmItemAdapter newAdapter = new AlarmItemAdapter(alarmList, getContext());
//                        listView.setAdapter(newAdapter);
//                        //在此处写入ok的逻辑
//                    }
//                });
//                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override//设置取消事件
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        System.out.println("cancel");
//                        //在此写入取消的事件
//                    }
//                });
//                dialog.show();
//                return true;
//                new AlertDialog.Builder(getView()).setTitle("操作").setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which){
//                            case 0:
//                                deleteAlarm(position);
//                                reRender();
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }).setNegativeButton("取消",null).show();
//                return true;
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        SharedPreferences mySharedPreferences= requireActivity().getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySharedPreferences.edit();
//        editor.clear();
//        editor.apply();
    }

//    private void deleteAlarm(int position){
//        alarmList.remove(position);
//    }
//
//    private void reRender(){
//        AlarmItemAdapter adapter = new AlarmItemAdapter(alarmList, getContext());
//        ListView listView = requireView().findViewById(R.id.AlarmCardList);
//        listView.setAdapter(adapter);
//    }
}