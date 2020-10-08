package com.example.wowtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.wowtime.databinding.ActivityMainBinding;

import com.example.wowtime.util.SensorManagerHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PersonInfo.class)));
//        Button button2 = findViewById(R.id.button3);
//        button2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,TaskListActivity.class)));

        ActivityMainBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar(binding.toolbarMain);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_person_info,R.id.navigation_clock,R.id.navigation_away_from_phone,R.id.navigation_friends,
                R.id.navigation_statistics,R.id.navigation_achievement).setDrawerLayout(binding.drawer).build();
        binding.fragmentMain.post(() -> {
            NavController navController = Navigation.findNavController(this, R.id.fragment_main);
            NavigationUI.setupWithNavController(binding.toolbarMain, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navigationMain, navController);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
