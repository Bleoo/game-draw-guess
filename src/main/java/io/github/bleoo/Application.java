package io.github.bleoo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.IOException;

/**
 * @Author: Bleoo
 * @Date: 2019-10-31 17:46
 */

@SpringBootApplication
public class Application {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String getJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public static <T> T getObj(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(
                Application.class
        );
        ConfigurableApplicationContext applicationContext = app.run(args);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
