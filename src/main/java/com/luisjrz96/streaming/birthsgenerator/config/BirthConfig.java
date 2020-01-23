package com.luisjrz96.streaming.birthsgenerator.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@ConfigurationProperties(prefix = "spring.application.custom.config")
public class BirthConfig {

    private HashMap<String, Integer> birth;

    public HashMap<String, Integer> getBirth() {
        return birth;
    }

    public void setBirth(HashMap<String, Integer> birth) {
        this.birth = birth;
    }
}

