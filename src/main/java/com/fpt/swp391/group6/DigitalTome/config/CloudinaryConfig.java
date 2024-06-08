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
                "cloud_name", "dagoilb3m",
                "api_key", "767652613148981",
                "api_secret", "UkmzV_xu5kj9F4dVa3kLPGD0q-Q"));
        return cloudinary;
    }


}
