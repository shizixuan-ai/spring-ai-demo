package com.example.spring_ai_demo.tools;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class FileOperationToolTest {

    @Test
    public void testReadFile(){
        FileOperationTool tool = new FileOperationTool();
        String result = tool.readFile("test.txt");
        Assertions.assertNotNull(result);

    }

    @Test
    public void testWriteFile(){
        FileOperationTool tool = new FileOperationTool();
        String result = tool.writeFile("test.txt", "test123");
        Assertions.assertNotNull(result);
    }

}
