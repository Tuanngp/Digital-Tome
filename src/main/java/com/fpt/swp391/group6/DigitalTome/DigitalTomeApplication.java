package com.fpt.swp391.group6.DigitalTome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DigitalTomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalTomeApplication.class, args);
    }
}
