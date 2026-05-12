package com.example.imagesearchmcpserverdemo.configuration;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfiguration {

    @Bean
    public ToolCallbackProvider getImageSearchTool(ImageSearchTool tool) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(tool)
                .build();
    }
}
