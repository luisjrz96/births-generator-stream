package com.luisjrz96.streaming.birthsgenerator.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.application.custom.config")
public class BirthConfig {

    private Map<String, Integer> birth;

    public Map<String, Integer> getBirth() {
        return birth;
    }

    public void setBirth(Map<String, Integer> birth) {
        this.birth = birth;
    }
}

