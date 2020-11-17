package com.example.wowtime.websocket;

import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class TWebSocketClient extends WebSocketClient {

    private Handler handler = new Handler(message -> {
        if (message.what == 1) {
            String response = (String) message.obj;
            JSONObject jsonObject = JSONObject.parseObject(response);
            System.out.println("response:" + response);
            String msg = null;
            msg = jsonObject.getString("msg");
//            Toast toast = Toast.makeText(ContextUtil.getInstance(),response,Toast.LENGTH_SHORT);
//            String msg = message.getData().get("msg").toString();
//            String data = message.getData().get("data").toString();
//            Toast toast = Toast.makeText(ContextUtil.getInstance(),msg,Toast.LENGTH_SHORT);
//            toast.show();
            if (msg.equals("remain friend request") || msg.equals("new friend request")) {
                UserInfoAfterLogin.webSocketMessage = true;
            }
//            Intent intent = new Intent();
//            intent.setAction("friend request");
//            sendBroadcast(intent);
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
