package io.github.bleoo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Bleoo
 * @Date: 2019-11-1 10:46
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private ResponseCode code;
    private String text;
    private String people;

    public Response(ResponseCode code, String text) {
        this.code = code;
        this.text = text;
    }

    @JsonIgnore
    public String getJson() throws JsonProcessingException {
        return Application.getJson(Response.this);
    }

}
