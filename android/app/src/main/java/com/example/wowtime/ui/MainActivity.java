package com.example.wowtime.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.preference.PreferenceManager;
import com.example.wowtime.MainApplication;
import com.example.wowtime.OnboardingActivity;
import com.example.wowtime.R;
import com.example.wowtime.databinding.ActivityMainBinding;
import com.example.wowtime.ui.alarm.AlarmListFragment;
import com.example.wowtime.ui.alarm.ClockSettingActivity;
import com.example.wowtime.ui.others.BoardingSupportFragment;
import com.example.wowtime.ui.others.FriendsListFragment;
import com.example.wowtime.ui.account.InternetFriendListActivity;
import com.example.wowtime.ui.others.SpeechRecognizeActivity;
import com.example.wowtime.ui.pomodoro.PomodoroListFragment;
import com.example.wowtime.ui.pomodoro.PomodoroSettingActivity;
import com.example.wowtime.util.UserInfoAfterLogin;
import com.example.wowtime.websocket.TWebSocketClientService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    SharedPreferences mainSp;
    Integer themeNumber;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MainActivity create !");
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingSupportFragment
        if (!sharedPreferences.getBoolean(
                BoardingSupportFragment.COMPLETED_ONBOARDING_PREF_NAME, false)) {
            // The user hasn't seen the OnboardingSupportFragment yet, so show it
            startActivity(new Intent(this, OnboardingActivity.class));
        }
        mainSp = super.getSharedPreferences("theme", MODE_PRIVATE);
        themeNumber = mainSp.getInt("theme", 0);
        MainApplication.setThemeNumber(themeNumber);
        if (themeNumber == 1) { setTheme(R.style.DarkTheme); } else { setTheme(R.style.AppTheme); }
//        setContentView(R.layout.main_used_to_debug);
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PersonInfo.class)));
//        Button button2 = findViewById(R.id.button3);
//        button2.setOnClickListener(
//                v -> startActivity(new Intent(MainActivity.this, TaskListActivity.class)));

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (themeNumber == 1) {
            DrawerLayout drawerLayout = findViewById(R.id.drawer);
            drawerLayout.setBackgroundColor(Color.parseColor("#aaaaaa"));
        }
        setSupportActionBar(binding.toolbarMain);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_person_info, R.id.navigation_sunflower, R.id.navigation_clock,
                R.id.navigation_away_from_phone, R.id.navigation_friends,
                R.id.navigation_statistics, R.id.navigation_achievement)
                .setDrawerLayout(binding.drawer).build();
        binding.fragmentMain.post(() -> {
            NavController navController = Navigation.findNavController(this, R.id.fragment_main);
            NavigationUI.setupWithNavController(binding.toolbarMain, navController,
                                                appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navigationMain, navController);
        });
        sharedPreferences = getSharedPreferences("userInfo",
                                                                   Context.MODE_PRIVATE);
        Integer userId = sharedPreferences.getInt("userId", -1);
        String userName = sharedPreferences.getString("userNmae", "");
        UserInfoAfterLogin.userid = userId;
        UserInfoAfterLogin.username = userName;

        if (userId != -1) {
            Intent startIntent = new Intent(MainActivity.this,
                                            TWebSocketClientService.class);
            startService(startIntent);
        }

        FloatingActionButton button = findViewById(R.id.fab_main);
        button.setOnClickListener(v -> {
            Class<? extends Fragment> c = getPrimaryFragmentClass();
            if (c == AlarmListFragment.class) {
                startActivity(new Intent(MainActivity.this, ClockSettingActivity.class));
            }
            if (c == PomodoroListFragment.class) {
                startActivity(new Intent(MainActivity.this, PomodoroSettingActivity.class));
            }
            if ((c == FriendsListFragment.class) && (UserInfoAfterLogin.userid != -1)) {
                startActivity(new Intent(MainActivity.this, InternetFriendListActivity.class));
            }
        });

        button.setOnLongClickListener(v -> {
            Class<? extends Fragment> c = getPrimaryFragmentClass();
            if (c == AlarmListFragment.class) {
                startActivity(new Intent(MainActivity.this, SpeechRecognizeActivity.class));
            }
            return false;
        });
//        Intent startIntent = new Intent(this, TWebSocketClientService.class);
//        startService(startIntent);
//        startForegroundService(startIntent);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(startIntent);
//        } else {
//            startService(startIntent);
//        }

        System.out.println("MainActivity create done!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_header_menu, menu);
        return true;
    }

    private Fragment getCFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_main);
    }

    public Class<? extends Fragment> getPrimaryFragmentClass() {
        for (Fragment fragment : getCFragment().getChildFragmentManager().getFragments()) {
            if (fragment != null) { return fragment.getClass(); }
        }
        return null;
    }

    @Override
    protected void onRestart() {
        super.recreate();
        super.onRestart();
    }
}
