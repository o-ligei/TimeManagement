package com.example.wowtime.ui.pomodoro;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import com.example.wowtime.R;
import com.example.wowtime.dto.WhiteListItem;

public class WhiteListActivity extends ListActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemLongClickListener {

    private ListView lv_app_list;
    private AppAdapter mAppAdapter;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list_activity);

//        LinkedList<AppListItem> appListItems = new LinkedList<>();
//
//        appListItems.add(new AppListItem("WeChat",R.drawable.wechat));
//        appListItems.add(new AppListItem("QQ",R.drawable.qq));
//
//        AppItemAdapter appItemAdapter = new AppItemAdapter(appListItems,getApplicationContext());
//
//        ListView listView = (ListView) findViewById(R.id.white_list);
//        listView.setAdapter(appItemAdapter);
        lv_app_list = (ListView) findViewById(android.R.id.list);
        mAppAdapter = new AppAdapter();
        lv_app_list.setAdapter(mAppAdapter);
        initAppList();

        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("whiteListPosition:"+position);
                System.out.println("whiteListId:"+id);
            }
        });

        getListView().setOnItemLongClickListener(this);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
//        return super.onCreateOptionsMenu(menu);
//    }




    private void initAppList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<WhiteListItem> appInfos = ApkTool.scanLocalInstallAppList(WhiteListActivity.this.getPackageManager());
                if(appInfos==null)
                    Toast.makeText(getApplicationContext(),"获取应用失败",Toast.LENGTH_SHORT).show();
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
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            } else {
                CheckBox cb = (CheckBox) convertView.findViewById(R.id.WhiteListCheckBox);
                // 小技巧：checkBox 的  tag 为它所在的行，在onCheckedChanged方法里面用到
                cb.setTag(position);
                cb.setOnCheckedChangeListener(WhiteListActivity.this);

                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(whiteListItem.getImage());
            mViewHolder.tx_app_name.setText(whiteListItem.getAppName());
            return convertView;
        }

        class ViewHolder {

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