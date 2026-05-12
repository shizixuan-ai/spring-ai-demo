package com.example.spring_ai_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application.yaml")
@ConfigurationProperties(prefix = "spring.ai")
public class BaseConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    private String dashScopeUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
}

