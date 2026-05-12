package com.example.spring_ai_demo.rag.demo;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MultiQueryExpanderDemoTest {

    @Autowired
    private MultiQueryExpanderDemo expander;

    @Test
    public void testExpand() {
        List<Query> queries = expander.expand("啥是程序员白玄啊啊啊啊啊啊？！请回答我哈哈哈哈");
        Assertions.assertNotNull(queries);
    }

}
