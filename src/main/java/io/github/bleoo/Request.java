package io.github.bleoo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Bleoo
 * @Date: 2019-11-1 10:46
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Request {

    private Command command;
    private String text;

}
