package com.example.wowtime.ui.alarm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.TaskItemAdapter;
import com.example.wowtime.dto.TaskListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);

        ArrayList<TaskListItem> taskListItems = new ArrayList<>();

        taskListItems.add(new TaskListItem("随机"));
        taskListItems.add(new TaskListItem("快速算算算"));
        taskListItems.add(new TaskListItem("使劲摇摇摇"));
        taskListItems.add(new TaskListItem("努力吹吹吹"));

        TaskItemAdapter taskItemAdapter = new TaskItemAdapter(taskListItems,getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(taskItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences mySharedPreferences= getSharedPreferences("clock", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
//                System.out.println(taskItemAdapter.getItem(position).toString());
                editor.putString("game", taskItemAdapter.getItem(position).toString());
                editor.apply();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
}
//public class TaskListActivity extends Fragment{
//    public TaskListActivity() {
//    }
//
//    public TaskListActivity(int contentLayoutId) {
//        super(contentLayoutId);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.task_list_activity, container, false);
//        ArrayList<TaskListItem> taskListItems = new ArrayList<>();
//
//        taskListItems.add(new TaskListItem("快速算算算"));
//        taskListItems.add(new TaskListItem("使劲摇摇摇"));
//        taskListItems.add(new TaskListItem("努力吹吹吹"));
//
//        TaskItemAdapter taskItemAdapter = new TaskItemAdapter(taskListItems,getContext());
//
//        ListView listView = root.findViewById(R.id.task_list);
//        listView.setAdapter(taskItemAdapter);
//        return root;
//    }
//}