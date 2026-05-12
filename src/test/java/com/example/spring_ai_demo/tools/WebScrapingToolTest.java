package com.example.spring_ai_demo.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WebScrapingToolTest {
    
    @Test
    public void testScrapeWebPage(){
        WebScrapingTool tool = new WebScrapingTool();
        String result = tool.scrapeWebPage("https://www.baidu.com");
        Assertions.assertNotNull(result);
    }
}
