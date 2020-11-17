package com.example.wowtime.ui.others;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.adapter.CreditDetailListAdapter;
import com.example.wowtime.dto.CreditDetailListItem;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import org.json.JSONException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;
import com.alibaba.fastjson.*;

public class CreditDetailListActivity extends AppCompatActivity {
    private ArrayList<CreditDetailListItem> creditDetailListItems;
    private CreditDetailListAdapter creditDetailListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_detail_list_activity);

        Timestamp initTime = new Timestamp(System.currentTimeMillis());
        creditDetailListItems = new ArrayList<>();
        fetchCredit();
//        creditDetailListItems.add(new CreditDetailListItem(initTime, 2, "IncreaseEvent1"));
//        creditDetailListItems.add(new CreditDetailListItem(initTime, 3, "IncreaseEvent2"));
//        creditDetailListItems.add(new CreditDetailListItem(initTime, -3, "DecreaseEvent3"));
        creditDetailListAdapter = new CreditDetailListAdapter(creditDetailListItems, getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.credit_detail_list);
        listView.setAdapter(creditDetailListAdapter);
    }

    public static String dealDateFormat(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return df2.format(date1);
    }

    private void fetchCredit(){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("userid", UserInfoAfterLogin.userid.toString());
        formBody.add("timestamp","whole");

        android.os.Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
                String msg= message.getData().get("msg").toString();
                String data=message.getData().get("data").toString();
                System.out.println("msg: "+msg);
                System.out.println("data: "+data);
                if(msg.equals("success")){
                    JSONArray jsArr = JSONObject.parseArray(data);
                    for(int i=0;i<jsArr.size();i++){
                        String event=jsArr.getJSONObject(i).get("event").toString();
                        String infos[]=event.split(" ");
                        creditDetailListItems.add(new CreditDetailListItem(Timestamp.valueOf(dealDateFormat(jsArr.getJSONObject(i).get("timestamp").toString())),
                                Integer.parseInt(infos[1]), infos[0]));
                    }
                    creditDetailListAdapter.notifyDataSetChanged();
                }
                else{
                    System.out.println("failed");
                }
            }
            return false;
        });

        Ajax ajax=new Ajax("/Detail/GetDetail",formBody,handler,InternetConstant.FETCH);
        ajax.fetch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
}