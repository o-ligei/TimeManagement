package com.example.wowtime.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Handler;


import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;

import java.net.URI;

public class TWebSocketClientService extends Service {
    private URI uri = URI.create(InternetConstant.websocket_host +"/Socket/"+ UserInfoAfterLogin.userid);
    public TWebSocketClient client = new TWebSocketClient(uri);
    private TWebSocketClientBinder mBinder;

    class TWebSocketClientBinder extends Binder {
        public void doTask(){
            System.out.println("doTask");
        }
    }
    public TWebSocketClientService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service created!");
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service onStartCommand!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service created!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(mBinder != null)
            return mBinder;
        return null;
    }

//    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
//    private Handler mHandler = new Handler();
//    private Runnable heartBeatRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (client != null) {
//                if (client.isClosed()) {
//                    reconnectWs();
//                }
//            } else {
//                //如果client已为空，重新初始化websocket
//                initSocketClient();
//            }
//            //定时对长连接进行心跳检测
//            mHandler.postDelayed(this, HEART_BEAT_RATE);
//        }
//    };
//
//    /**
//     * 开启重连
//     */
//    private void reconnectWs() {
//        mHandler.removeCallbacks(heartBeatRunnable);
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //重连
//                    client.reconnectBlocking();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
}
