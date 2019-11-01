package io.github.bleoo;

import lombok.Data;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: Bleoo
 * @Date: 2019-10-31 17:59
 */

@Data
public class Room {

    public static final int drawAroundTime = 60;
    public static final int pickWordTime = 20;

    public List<People> allPeopleList = new ArrayList<>();

    private RoomState state = RoomState.WAITING;
    private int drawerIndex;
    private People drawer;
    private String drawWord;
    private Timer pickTimer;
    private Timer drawTimer;

    public People addPeople(String name, Session session) {
        People people = new People();
        people.setName(name);
        people.setSession(session);
        people.setRoom(this);
        synchronized (allPeopleList) {
            if (allPeopleList.size() < 6) {
                allPeopleList.add(people);
            } else {
                return null;
            }
        }
        try {
            publishText(name + "进入了房间");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return people;
    }

    public People findPeople(String name) {
        for (People people : allPeopleList) {
            if (people.getName().equals(name)) {
                return people;
            }
        }
        return null;
    }

    public People findPeople(Session session) {
        for (People people : allPeopleList) {
            if (people.getSession().equals(session)) {
                return people;
            }
        }
        return null;
    }

    public void removePeople() {

    }

    private void broadcastAll(String text) throws IOException {
        synchronized (allPeopleList) {
            for (People other : allPeopleList) {
                if (other.getSession().isOpen()) {
                    other.getSession().getBasicRemote().sendText(text);
                }
            }
        }
    }

    public void publishText(String text) throws Exception {
        Response response = new Response(ResponseCode.ROOM_PUSH_TEXT, text);
        String json = response.getJson();
        broadcastAll(json);
    }

    public void peopleTalkAll(People people, String text) throws Exception {
        Response response = new Response(ResponseCode.PEOPLE_TALK, text);
        response.setPeople(people.getName());
        String json = response.getJson();
        broadcastAll(json);
    }

    public void peopleDraw(People people, String text) throws Exception {
        Response response = new Response(ResponseCode.PEOPLE_DRAW, text);
        response.setPeople(people.getName());
        String json = response.getJson();
        broadcastAll(json);
    }

    public void checkPeopleState() {
        synchronized (allPeopleList) {
            if (allPeopleList.size() == 6) {
                for (People people : allPeopleList) {
                    if (people.getState() == PeopleState.UNREADY) {
                        return;
                    }
                }
                // 游戏开始
                gameStart();
            }
        }
    }

    private void gameStart() {
        if (state == RoomState.PLAYING) {
            return;
        }
        state = RoomState.PLAYING;
        try {
            publishState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            startDrawLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void publishState() throws Exception {
        Response response = new Response(ResponseCode.GAME_START, "游戏开始了");
        String json = response.getJson();
        broadcastAll(json);
    }

    private void startDrawLoop() throws Exception {
        drawer = allPeopleList.get(drawerIndex);
        sendWordList();
        drawerIndex++;
    }

    private void sendWordList() throws Exception {
        Response response = new Response(ResponseCode.WORD_LIST, "a,b,c,d");
        String json = response.getJson();
        if (drawer.getSession().isOpen()) {
            drawer.getSession().getBasicRemote().sendText(json);
        }
        pickTimer = new Timer();
        pickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (drawWord == null) {
                    try {
                        startDrawLoop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, pickWordTime * 1000);
    }

    public void savePickWord(People people, String text) throws Exception {
        if (people.equals(drawer)) {
            drawWord = text;
            stopPickTimer();
        } else {
            Response response = new Response(ResponseCode.PICK_TIMEOUT, "");
            String json = response.getJson();
            if (drawer.getSession().isOpen()) {
                drawer.getSession().getBasicRemote().sendText(json);
            }
        }
    }

    public void stopPickTimer() {
        pickTimer.cancel();
        pickTimer = null;
        drawTimer = new Timer();
        drawTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    startDrawLoop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, drawAroundTime * 1000);
    }

    public void stopDrawTimer() {
        drawTimer.cancel();
        drawTimer = null;
    }

}
