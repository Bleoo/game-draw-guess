package io.github.bleoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Bleoo
 * @Date: 2019-11-1 10:34
 */
@AllArgsConstructor
public enum Command {

    READY(1),
    UNREADY(2),
    TEXT(3),
    DRAW(4),
    PICK_WORD(5),
    ;

    @Getter
    private int code;

    @JsonCreator
    public static Command get(int code) {
        for (Command value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

}
