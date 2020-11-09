package com.example.wowtime.ui.pomodoro;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.WhiteListItem;

public class WhiteListActivity extends ListActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemLongClickListener {

    private ListView lv_app_list;
    private AppAdapter mAppAdapter;
    private int fromScreen;
    private List<String> alreadySelectedPackage;
    private SharedPreferences pomodoroSp;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list_activity);
        pomodoroSp=super.getSharedPreferences("pomodoro",MODE_PRIVATE);
        String s=pomodoroSp.getString("whitelist","");
        if(s.equals(""))
            alreadySelectedPackage =new LinkedList<>();
        else{
            alreadySelectedPackage = JSONObject.parseArray(s,String.class);
        }

//        LinkedList<AppListItem> appListItems = new LinkedList<>();
//
//        appListItems.add(new AppListItem("WeChat",R.drawable.wechat));
//        appListItems.add(new AppListItem("QQ",R.drawable.qq));
//
//        AppItemAdapter appItemAdapter = new AppItemAdapter(appListItems,getApplicationContext());
//
//        ListView listView = (ListView) findViewById(R.id.white_list);
//        listView.setAdapter(appItemAdapter);
        Intent intent=getIntent();
        fromScreen=intent.getIntExtra("fromScreenSaver",0);
        lv_app_list = (ListView) findViewById(android.R.id.list);
        mAppAdapter = new AppAdapter();
        initAppList(fromScreen);
        lv_app_list.setAdapter(mAppAdapter);

//        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("whiteListPosition:"+position);
//                System.out.println("whiteListId:"+id);
//            }
//        });

        //getListView().setOnItemLongClickListener(this);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(fromScreen==0) {
            SharedPreferences.Editor editor=pomodoroSp.edit();
            String s=JSONObject.toJSONString(alreadySelectedPackage);
            System.out.println("whitelistSave:"+s);
            editor.putString("whitelist",s);
            editor.apply();
        }
    }

    private void initAppList(int fromScreen){
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<WhiteListItem> appInfos;
                if(fromScreen==0) {
                    //扫描得到APP列表
                    appInfos = ApkTool.scanLocalInstallAppList(WhiteListActivity.this.getPackageManager(), alreadySelectedPackage);
                    if (appInfos == null)
                        Toast.makeText(getApplicationContext(), "获取应用失败", Toast.LENGTH_SHORT).show();
                }
                else{
                    appInfos=ApkTool.getSelectedPackageList(WhiteListActivity.this.getPackageManager(), alreadySelectedPackage);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfos);
                    }
                });
            }
        }.start();
    }

    class AppAdapter extends BaseAdapter {

        List<WhiteListItem> whiteListItems = new ArrayList<WhiteListItem>();

        public void setData(List<WhiteListItem> whiteListItems) {
            this.whiteListItems = whiteListItems;
            notifyDataSetChanged();
        }

        public List<WhiteListItem> getData() {
            return whiteListItems;
        }

        @Override
        public int getCount() {
            if (whiteListItems != null && whiteListItems.size() > 0) {
                return whiteListItems.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (whiteListItems != null && whiteListItems.size() > 0) {
                return whiteListItems.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            WhiteListItem whiteListItem = whiteListItems.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.app_info_item, null);
                mViewHolder.cb = (CheckBox) convertView.findViewById(R.id.WhiteListCheckBox);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            } else {
                CheckBox cb = (CheckBox) convertView.findViewById(R.id.WhiteListCheckBox);
                // 小技巧：checkBox 的  tag 为它所在的行，在onCheckedChanged方法里面用到
                cb.setTag(position);
                cb.setOnCheckedChangeListener(WhiteListActivity.this);

                mViewHolder = (ViewHolder) convertView.getTag();
                ImageView icon=convertView.findViewById(R.id.iv_app_icon);
                // 小技巧：checkBox 的  tag 为它所在的行，在onCheckedChanged方法里面用到
                if(fromScreen==0) {
                    cb.setTag(position);
                    cb.setOnClickListener(v -> {
                        System.out.println("test" + whiteListItems.get(position).getAppName());
                        String packageName=whiteListItems.get(position).getPackageName();
                        if(alreadySelectedPackage.indexOf(packageName)==-1) {
                            alreadySelectedPackage.add(packageName);
                            System.out.println("whitelistSelect:"+packageName);
                        }
                        else {
                            alreadySelectedPackage.remove(packageName);
                            System.out.println("whitelistRemove:"+packageName);
                        }
                    });
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            while(whiteListItems.isEmpty()){
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    System.out.println("interrupt???");
                                    e.printStackTrace();
                                }
                            }
                            cb.setChecked(whiteListItems.get(position).getSelected());
                        }
                    }.start();
//                    cb.setSelected(whiteListItems.get(position).getSelected());
                }
                else{
                    PackageManager packageManager = getPackageManager();
                    icon.setOnClickListener(v->{
                        System.out.println("whitelistImageclick:"+whiteListItems.get(position).getPackageName());
                        Intent intent = packageManager.getLaunchIntentForPackage(whiteListItems.get(position).getPackageName());
                        startActivity(intent);
                    });
                    mViewHolder.cb.setVisibility(View.INVISIBLE);
//                    cb.setVisibility(View.GONE);
                    //cb.setWillNotDraw(false);
                }
//                cb.setOnCheckedChangeListener(WhiteListActivity.this);
            }
            mViewHolder.iv_app_icon.setImageDrawable(whiteListItem.getImage());
            mViewHolder.tx_app_name.setText(whiteListItem.getAppName());
            return convertView;
        }

        class ViewHolder {
            CheckBox cb;
            ImageView iv_app_icon;
            TextView tx_app_name;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int row = (Integer) buttonView.getTag();
        Toast.makeText(this, "第"+row+"行的checkBox被点击", Toast.LENGTH_LONG).show();
    }

    public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
        Toast.makeText(this, "第"+position+"行被长按", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this, "第"+position+"行被点击", Toast.LENGTH_LONG).show();
    }
}