package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PersonInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        View personInfoCard = findViewById(R.id.PersonInfoCard);
        personInfoCard.setOnClickListener(v -> startActivity(new Intent(PersonInfo.this, PersonInfoDetail.class)));
    }
}