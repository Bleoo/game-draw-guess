package io.github.bleoo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.websocket.Session;

/**
 * @Author: Bleoo
 * @Date: 2019-10-31 17:59
 */

@Data
@EqualsAndHashCode(of = "name")
public class People {

    private String name;
    private PeopleState state;
    private Room room;
    private Session session;

    public void say2Room(String text) {
        if (room.getState() == RoomState.PLAYING) {
            if (text.equals(room.getDrawWord())) {
                try {
                    room.publishText(name + "猜对了");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        try {
            room.peopleTalkAll(this, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ready() {
        if (room.getState() == RoomState.WAITING || state == PeopleState.UNREADY) {
            state = PeopleState.READY;
        }
        room.checkPeopleState();
    }

    public void unready() {
        if (room.getState() == RoomState.WAITING || state == PeopleState.READY) {
            state = PeopleState.UNREADY;
        }
    }

    public void draw(String text) {
        try {
            room.peopleDraw(this, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickWord(String text) {
        try {
            room.savePickWord(this, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

