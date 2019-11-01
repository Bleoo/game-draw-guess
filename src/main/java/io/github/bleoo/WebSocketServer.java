package io.github.bleoo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @Author: Bleoo
 * @Date: 2019-10-31 18:45
 */

@Slf4j
@ServerEndpoint("/websocket/{name}")
@Component
public class WebSocketServer {
    public static Room room = new Room();

    private Session session;
    private People people;

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) {
        this.session = session;
        if (room.getState() == RoomState.PLAYING) {
            sendError("正在对局中");
            return;
        }
        people = room.addPeople(name, session);
        if (people == null) {
            sendError("房间满了，请开充值");
        }
    }

    @OnClose
    public void onClose() {
        log.info("一个客户端关闭连接");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        Request request = null;
        try {
            request = Application.getObj(message, Request.class);
        } catch (IOException e) {
            sendError("消息不合法");
        }
        if (request == null) {
            return;
        }
        log.info(request.toString());
        switch (request.getCommand()) {
            case READY:
                people.ready();
                break;
            case UNREADY:
                people.unready();
                break;
            case TEXT:
                people.say2Room(request.getText());
                break;
            case DRAW:
                people.draw(request.getText());
                break;
            case PICK_WORD:
                people.pickWord(request.getText());
                break;
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket出现错误");
        error.printStackTrace();
    }

    private void sendError(String text) {
        Response response = new Response(ResponseCode.ERROR, text);
        try {
            session.getBasicRemote().sendText(response.getJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
