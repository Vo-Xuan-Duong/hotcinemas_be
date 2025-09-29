package com.example.hotcinemas_be.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    public Cloudinary cloudinary() {
        return new Cloudinary(cloudinaryUrl);
    }
}
