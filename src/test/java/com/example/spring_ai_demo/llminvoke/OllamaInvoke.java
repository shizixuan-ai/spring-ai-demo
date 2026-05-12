package com.example.spring_ai_demo.llminvoke;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.AssistantMessage;



@SpringBootTest
@Component
public class OllamaInvoke implements CommandLineRunner{
    @Resource
    private ChatModel ollamaChatModel;
    
    @Override
    public void run(String... args) throws Exception {
        String prompt = "Hello, how are you?";
        
        AssistantMessage response = ollamaChatModel.call(new Prompt(prompt)).getResult().getOutput();
        System.out.println(response);
    }


}
