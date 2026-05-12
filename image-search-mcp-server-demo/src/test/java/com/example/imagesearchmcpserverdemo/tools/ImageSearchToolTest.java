package com.example.imagesearchmcpserverdemo.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageSearchToolTest {

    @Autowired
    private ImageSearchTool tool;

    @Test
    public void testSearchImage() {
        String result = tool.searchImage("computer");
        Assertions.assertNotNull(result);
    }
}
