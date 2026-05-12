package com.example.spring_ai_demo.rag.demo;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

/**
 * 查询扩展器
 */
@Component
public class MultiQueryExpanderDemo {

    private ChatClient.Builder builder;

    public MultiQueryExpanderDemo(Builder builder) {
        this.builder = builder;
    }

    public List<Query> expand(String query){
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
        .chatClientBuilder(builder).numberOfQueries(3).build();
        return multiQueryExpander.expand(new Query("谁是白玄？"));
    }
    
}
