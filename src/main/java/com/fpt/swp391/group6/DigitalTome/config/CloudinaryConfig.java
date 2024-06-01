package com.fpt.swp391.group6.DigitalTome.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "Your Cloudinary",
                "api_key", "Your Api Key",
                "api_secret", "Your secret here"));
        return cloudinary;
    }


}
