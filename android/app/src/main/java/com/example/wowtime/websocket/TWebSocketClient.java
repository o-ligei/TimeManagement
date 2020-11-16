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
import com.example.wowtime.util.UserInfoAfterLogin;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

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

    private Handler handler = new Handler(message -> {
        if (message.what == 1) {
            String response = (String) message.obj;
//            System.out.println("getData:"+message.getData());
//            System.out.println(response);
//            String msg = null;
//            try {
//                msg = response.getString("msg");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            Toast toast = Toast.makeText(ContextUtil.getInstance(),response,Toast.LENGTH_SHORT);
//            String msg = message.getData().get("msg").toString();
//            String data = message.getData().get("data").toString();
//            Toast toast = Toast.makeText(ContextUtil.getInstance(),msg,Toast.LENGTH_SHORT);
            toast.show();
//            if(msg.equals("remain friend request"))
                UserInfoAfterLogin.webSocketMessage = true;
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


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("TWebSocketClient onClose()");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("TWebSocketClient onError()");
    }
}
