package com.example.wowtime.ui.others;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.wowtime.R;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.util.Calendar;
import okhttp3.FormBody;
import org.json.JSONException;

public class Sunflower extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        TextView CreditNumber = findViewById(R.id.CreditNumber);
        CreditNumber.setOnClickListener(
                v -> startActivity(new Intent(Sunflower.this, CreditDetailListActivity.class)));
        fetchCredit();
        checkTime();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        ConstraintLayout layout = findViewById(R.id.CreditConstraintLayout);
        if (hour >= 6 && hour <= 10) {
            layout.setBackground(getResources().getDrawable(R.drawable.morning));
            return;
        }
        if (hour > 10 && hour <= 15) {
            layout.setBackground(getResources().getDrawable(R.drawable.midday));
            return;
        }
        if (hour > 15 && hour < 19) {
            layout.setBackground(getResources().getDrawable(R.drawable.dusk));
            return;
        }
        layout.setBackground(getResources().getDrawable(R.drawable.night));
    }

    private void fetchCredit() {
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("userid", UserInfoAfterLogin.userid.toString());

        @SuppressLint("SetTextI18n") android.os.Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
                String msg = message.getData().get("msg").toString();
                String data = message.getData().get("data").toString();
                System.out.println("msg" + msg);
                System.out.println("data" + data);
                org.json.JSONObject jsonObject = null;
                if (msg.equals("success")) {
                    try {
                        jsonObject = new org.json.JSONObject(data);
                        data = jsonObject.get("credit").toString();
                        jsonObject = new org.json.JSONObject(data);
                        data = jsonObject.get("score").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TextView credit_detail = findViewById(R.id.CreditNumber);
                    credit_detail.setText("credit:  " + data);
                } else {
                    System.out.println("failed");
                }
            }
            return false;
        });

        Ajax ajax = new Ajax("/User/GetPersonalCredit", formBody, handler, InternetConstant.FETCH);
        ajax.fetch();
    }
}