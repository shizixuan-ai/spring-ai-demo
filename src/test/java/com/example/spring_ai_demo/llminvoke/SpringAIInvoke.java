package com.example.spring_ai_demo.llminvoke;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import jakarta.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Component
public class SpringAIInvoke implements CommandLineRunner{

    @Resource
    private ChatModel model;


    @Override
    public void run(String... args) throws Exception {
        // ChatResponse response = model.call(new Prompt("你是谁？"));
        // System.out.println(response.getText());
    }

    @Test
    public void testSpringAiInvoke() {
        
    }
}
