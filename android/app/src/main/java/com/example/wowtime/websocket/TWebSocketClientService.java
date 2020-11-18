package com.example.wowtime.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.net.URI;

public class TWebSocketClientService extends Service {

    private URI uri = URI
            .create(InternetConstant.websocket_host + "/Socket/" + UserInfoAfterLogin.userid);
    public TWebSocketClient client;
    private TWebSocketClientBinder mBinder;

    class TWebSocketClientBinder extends Binder {

        public void doTask() {
            System.out.println("doTask");
        }
    }

    public TWebSocketClientService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service created!");
//        try {
//            client.connectBlocking();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service onStartCommand!");
        //初始化websocket
        initSocketClient();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测

//        //设置service为前台服务，提高优先级
//        if (Build.VERSION.SDK_INT < 18) {
//            //Android4.3以下 ，隐藏Notification上的图标
//            startForeground(GRAY_SERVICE_ID, new Notification());
//        } else if(Build.VERSION.SDK_INT>18 && Build.VERSION.SDK_INT<25){
//            //Android4.3 - Android7.0，隐藏Notification上的图标
//            Intent innerIntent = new Intent(this, GrayInnerService.class);
//            startService(innerIntent);
//            startForeground(GRAY_SERVICE_ID, new Notification());
//        }else{
//            //Android7.0以上app启动后通知栏会出现一条"正在运行"的通知
//            startForeground(GRAY_SERVICE_ID, new Notification());
//        }
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service created!");
        closeConnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder != null) { return mBinder; }
        return null;
    }

    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        client = new TWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                System.out.println("TWebSocketClientService"+ "收到的消息：" + message);
                Log.e("TWebSocketClientService", "收到的消息：" + message);
                JSONObject jsonObject = JSONObject.parseObject(message);
//                System.out.println("response:" + response);
                String msg = null;
                msg = jsonObject.getString("msg");
                if (msg.equals("remain friend request") || msg.equals("new friend request")) {
                    UserInfoAfterLogin.webSocketMessage = true;
                }
                Intent intent = new Intent();
                intent.setAction("friend request");
                sendBroadcast(intent);
//                checkLockAndShowNotification(message);
            }

        };
        connect();
    }
    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }

    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化连接
                client = null;
                initSocketClient();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
