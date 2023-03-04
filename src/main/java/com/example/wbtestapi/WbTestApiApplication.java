package com.example.wbtestapi;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.*;

@SpringBootApplication
public class WbTestApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(WbTestApiApplication.class, args);
    }
}
