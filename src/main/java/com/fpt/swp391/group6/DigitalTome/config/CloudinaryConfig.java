package com.fpt.swp391.group6.DigitalTome.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dznef2sae",
                "api_key", "537285485346489",
                "api_secret", "PHu0HBagBqf1rgfKMSJpSLUh29M",
                "secure", true));
    }
}
