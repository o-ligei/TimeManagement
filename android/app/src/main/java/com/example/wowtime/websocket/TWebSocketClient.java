package com.example.wowtime.websocket;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.wowtime.R;
import com.example.wowtime.ui.MainActivity;
import com.example.wowtime.ui.account.InternetFriendRequestActivity;
import com.example.wowtime.util.ContextUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import android.app.Notification;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

public class TWebSocketClient extends WebSocketClient {

    private Handler handler = new Handler(msg -> {
        if (msg.what == 1) {
            String response = (String) msg.obj;
            Toast toast = Toast.makeText(ContextUtil.getInstance(),response,Toast.LENGTH_SHORT);
            toast.show();
        }
        return false;
    });

    public TWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("TWebSocketClient onOpen()");
    }


    @Override
    public void onMessage(String message) {
        System.out.println("TWebSocketClient onMessage()" + message);
        Message m = Message.obtain();
        m.what = 1;
        m.obj = message;
        handler.sendMessage(m);
    }

//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(ContextUtil.getInstance())
//                        .setSmallIcon(R.drawable.wechat)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");
//        Intent resultIntent = new Intent(ContextUtil.getInstance(), InternetFriendRequestActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                ContextUtil.getInstance(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(resultPendingIntent);
//        Notification notification = mBuilder.build();
//        int mNotificationId = 001;
//
//        NotificationManager mNotifyMgr =
//                (NotificationManager) ContextUtil.getInstance().getSystemService(NOTIFICATION_SERVICE);
//
//        mNotifyMgr.notify(mNotificationId, notification);
//        String id = "my_channel_01";
//        String name="我是渠道名字";
//        NotificationManager notificationManager = (NotificationManager) ContextUtil.getInstance().getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
////            Toast.makeText(ContextUtil.getInstance(), mChannel.toString(), Toast.LENGTH_SHORT).show();
//            Log.i(TAG, mChannel.toString());
//            notificationManager.createNotificationChannel(mChannel);
//            notification = new Notification.Builder(ContextUtil.getInstance())
//                    .setChannelId(id)
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher).build();
//        } else {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ContextUtil.getInstance())
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setOngoing(true);
////                    .setChannel(id);//无效
//            notification = notificationBuilder.build();
//        }
//        notificationManager.notify(111123, notification);
        System.out.println("notification");
    }

//    private void sendNotification(String content) {
//        Intent intent = new Intent();
//        intent.setClass(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(this)
//                .setAutoCancel(true)
//                // 设置该通知优先级
//                .setPriority(Notification.PRIORITY_MAX)
//                .setSmallIcon(R.drawable.icon)
//                .setContentTitle("服务器")
//                .setContentText(content)
//                .setVisibility(VISIBILITY_PUBLIC)
//                .setWhen(System.currentTimeMillis())
//                // 向通知添加声音、闪灯和振动效果
//                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
//                .setContentIntent(pendingIntent)
//                .build();
//        notifyManager.notify(1, notification);//id要保证唯一
//    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("TWebSocketClient onClose()");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("TWebSocketClient onError()");
    }
}
