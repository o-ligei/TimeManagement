package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.wowtime.adapter.TaskItemAdapter;
import com.example.wowtime.component.TaskListItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        ArrayList<TaskListItem> taskListItems = new ArrayList<>();

        taskListItems.add(new TaskListItem("快速算算算"));
        taskListItems.add(new TaskListItem("使劲摇摇摇"));
        taskListItems.add(new TaskListItem("努力吹吹吹"));

        TaskItemAdapter taskItemAdapter = new TaskItemAdapter(taskListItems,getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(taskItemAdapter);
    }
}