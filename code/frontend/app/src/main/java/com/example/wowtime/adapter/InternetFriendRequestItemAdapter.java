package com.example.wowtime.adapter;

import static com.example.wowtime.util.UserInfoAfterLogin.webSocketMessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wowtime.R;
import com.example.wowtime.dto.InternetFriendItem;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.util.ArrayList;
import okhttp3.FormBody;

public class InternetFriendRequestItemAdapter extends BaseAdapter {

    private ArrayList<InternetFriendItem> mData;
    private Context mContext;
    private int cnt;

    public InternetFriendRequestItemAdapter(ArrayList<InternetFriendItem> mData, Context mContext) {

        this.mData = mData;
        this.mContext = mContext;
        this.cnt = mData.size();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext)
                                    .inflate(R.layout.internet_friend_request_item, parent, false);

        ImageView img_icon = (ImageView) convertView.findViewById(R.id.user_icon_internet_request);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.username_internet_request);
        Button add_friend = convertView.findViewById(R.id.add_friend_button_request);
//        add_friend.setOnClickListener(v->OKAddFriend(mData.get(position).getUserId()));
        View finalConvertView = convertView;
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OKAddFriend(mData.get(position).getUserId());
                add_friend.setText(
                        finalConvertView.getResources().getString(R.string.already_accept_friend));
                add_friend.setEnabled(false);
//                Intent intent = new Intent(mContext, ClockSettingActivity.class);
//                intent.putExtra("position",position);
//                mContext.startActivity(intent);
                if (--cnt == 0) { webSocketMessage = false; }
            }
        });

        String userIcon = mData.get(position).getUserIcon();
        byte[] decodedString = Base64.decode(userIcon, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        img_icon.setImageBitmap(decodedByte);
        txt_aName.setText(mData.get(position).getUsername());
        return convertView;
    }

    private Handler handler = new Handler(msg -> {
        if (msg.what == 1) {
            String response = (String) msg.obj;
            Toast toast = Toast.makeText(mContext, response, Toast.LENGTH_SHORT);
            toast.show();
        }
        return false;
    });

    private void OKAddFriend(Integer to) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder formBody = new FormBody.Builder();
                System.out.println(to);
                formBody.add("from", String.valueOf(UserInfoAfterLogin.userid));
                formBody.add("to", String.valueOf(to));
                System.out.println(
                        "from" + UserInfoAfterLogin.userid + "to" + to + "/Social/AcceptFriend");
                Ajax ajax = new Ajax("/Social/AcceptFriend", formBody, handler, 1);
                ajax.fetch();
            }
        }).start();
    }

}
