package com.oligei.timemanagement.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oligei.timemanagement.TimemanagementApplication;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.exceptions.NotOnlineException;
import com.oligei.timemanagement.service.SocialService;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import com.oligei.timemanagement.utils.msgutils.MsgConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/Socket/{userId}")
@Component
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(TimemanagementApplication.class);
    private static ConcurrentHashMap<String, WebSocketController> webSocketMap = new ConcurrentHashMap<>();
    private Session session;
    private String userId = "";

    @Autowired
    private SocialService socialService;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketMap.remove(userId);
        webSocketMap.put(userId, this);
        try {
            Msg<List<Profile>> existFriendRequest = socialService.getFriendRequest(Integer.valueOf(userId));
            existFriendRequest.setStatus(MsgConstant.REMAIN_FRIEND_REQUEST);
            existFriendRequest.setMsg(MsgConstant.REMAIN_FRIEND_REQUEST_MESSAGE);
            Msg<Boolean> msg = new Msg<>(MsgCode.SUCCESS);
            if (existFriendRequest.getData().isEmpty()) sendMessage((JSONObject) JSONObject.toJSON(msg), userId);
            else sendMessage((JSONObject) JSONObject.toJSON(existFriendRequest), userId);
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (NotOnlineException e) {
            logger.error("NotOnlineException", e);
        }
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(userId);
    }

    public void sendMessageHelper(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendMessage(JSONObject jsonObject, String toUserId) throws IOException, NotOnlineException {
        Objects.requireNonNull(jsonObject, "null message --WebSocketController sendMessage");
        Objects.requireNonNull(toUserId, "null toUserId --WebSocketController sendMessage");
        WebSocketController webSocketController = webSocketMap.get(toUserId);
        if (webSocketController == null) throw new NotOnlineException(MsgConstant.NOT_ONLINE_MESSAGE);
        String message = JSON.toJSONString(jsonObject);
        webSocketController.sendMessageHelper(message);
    }
}
