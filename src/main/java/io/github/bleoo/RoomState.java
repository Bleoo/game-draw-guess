package io.github.bleoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Bleoo
 * @Date: 2019-11-1 11:01
 */
@AllArgsConstructor
public enum RoomState {

    WAITING(0),
    PLAYING(1),
    ;

    @Getter
    private int code;

    @JsonCreator
    public static RoomState get(int code) {
        for (RoomState value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

}
