package io.github.bleoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Bleoo
 * @Date: 2019-11-1 11:01
 */
@AllArgsConstructor
public enum PeopleState {

    UNREADY(0),
    READY(1),
    PLAYING(2),
    ;

    @Getter
    private int code;

    @JsonCreator
    public static PeopleState get(int code) {
        for (PeopleState value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

}
