package com.example.spring_ai_demo.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    public void testSearchWeb(){
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String result = tool.searchWeb("baidu.com");
        Assertions.assertNotNull(result);
    }

}
