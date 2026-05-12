package com.example.spring_ai_demo.llminvoke;

import com.example.spring_ai_demo.config.BaseConfig;

import org.junit.jupiter.api.Test;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LangChainInvoke {

    @Autowired
    private BaseConfig constant;
    
    @Test
    public void testLangChain() {
        ChatLanguageModel model = QwenChatModel.builder()
                                                    .apiKey(constant.getApiKey())
                                                    .modelName("qwen-max")
                                                    .build();
        String response = model.chat("你是谁？");
        System.out.println(response);
    }
}
