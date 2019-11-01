package io.github.bleoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Bleoo
 * @Date: 2019-11-1 10:46
 */

@AllArgsConstructor
public enum ResponseCode {

    OK(0),
    ERROR(1),
    ROOM_PUSH_TEXT(2),
    PEOPLE_TALK(3),
    PEOPLE_DRAW(4),
    GAME_START(5),
    GAME_FINISH(6),
    WORD_LIST(7),
    PICK_TIMEOUT(8),

    ;

    @Getter
    @JsonValue
    private int code;

    @JsonCreator
    public static ResponseCode get(int code) {
        for (ResponseCode value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

}
