package com.virgo.rekomendasos.config;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dw1lc8ynz");
        config.put("api_key", "543951964988986");
        config.put("secret_key", "jl6mqzc8755R2nBAAiEweEqzP94");
        return new Cloudinary(config);
    }
}
