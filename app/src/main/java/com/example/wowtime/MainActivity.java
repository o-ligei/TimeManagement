package com.example.wowtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AnotherActivity.class)));
        Button button2 = findViewById(R.id.button3);
        button2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,LoginActivity2.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_header, menu);
        return true;
    }

}
