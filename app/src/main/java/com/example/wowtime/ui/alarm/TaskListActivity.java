package com.example.wowtime.ui.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.TaskItemAdapter;
import com.example.wowtime.dto.TaskListItem;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);

        ArrayList<TaskListItem> taskListItems = new ArrayList<>();

        taskListItems.add(new TaskListItem("快速算算算"));
        taskListItems.add(new TaskListItem("使劲摇摇摇"));
        taskListItems.add(new TaskListItem("努力吹吹吹"));

        TaskItemAdapter taskItemAdapter = new TaskItemAdapter(taskListItems,getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(taskItemAdapter);
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