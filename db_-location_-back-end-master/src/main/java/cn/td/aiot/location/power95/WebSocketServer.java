package cn.td.aiot.location.power95;


import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.exception.RedisConnectException;
import cn.td.aiot.common.service.RedisService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/location/{sid}")
@Component
public class WebSocketServer {


    private static RedisService redisService;
    private static Gson gson;

    @Autowired
    public void setSocketRedisService(RedisService redisService, Gson gson) {
        WebSocketServer.redisService = redisService;
        WebSocketServer.gson = gson;
    }


    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final CopyOnWriteArraySet<Session> SOCKET_SERVER = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        //加入set中
        SOCKET_SERVER.add(session);

    }


    /**
     * 发送信息
     */
    private void pushMsg() {
        try {
//            String s = redisService.get(TiduConstant.LOCATION_REALTIME_REDIS);
            List<String> devices = redisService.hvals(TiduConstant.LOCATION_REALTIME_REDIS);
            this.sendMessage(gson.toJson(devices));
        } catch (RedisConnectException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public String onMessage(String message) {
        System.out.println("接收指令" + message);
        while (true) {
            pushMsg();
        }
    }

    /**
     * 实现服务器主动推送
     *
     * @param message 信息
     */
    public void sendMessage(String message) {
        if (message != null) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * 群发自定义消息
     *
     * @param message 信息
     */
    public static void sendInfo(String message) {
        SOCKET_SERVER.forEach(client -> {
            try {
                client.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        SOCKET_SERVER.forEach(client -> {
            if (session.getId().equals(client.getId())) {
                SOCKET_SERVER.remove(session);
            }
        });
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接发生错误时候触发
     *
     * @param session session
     * @param error   错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        SOCKET_SERVER.forEach(client -> {
            if (session.getId().equals(client.getId())) {
                SOCKET_SERVER.remove(session);
                error.printStackTrace();
            }
        });
    }


    /**
     * 得到在线人数
     *
     * @return int
     */
    public static synchronized int getOnlineCount() {
        return SOCKET_SERVER.size();
    }

}
