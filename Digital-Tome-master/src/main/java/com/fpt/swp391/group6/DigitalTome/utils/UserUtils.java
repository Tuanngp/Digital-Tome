package com.fpt.swp391.group6.DigitalTome.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class UserUtils {

    public static String generateToken(){
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for(int i = 0; i < 4; i++){
            token.append(random.nextInt(10));
        }
        return token.toString();
    }

    public static boolean isTokenExpired(LocalDateTime tokenCreationDate){
        return Duration.between(tokenCreationDate, LocalDateTime.now()).toMinutes() > 1;
    }
}
