package com.oligei.timemanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TimemanagementApplication {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private static final Logger logger = LoggerFactory.getLogger(TimemanagementApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(TimemanagementApplication.class, args);
    }

}
