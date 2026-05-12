package com.example.spring_ai_demo.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest; 

@SpringBootTest
public class TerminalOperationToolTest {

    @Test
    public void testExecuteTerminalCommand(){
        TerminalOperationTool tool = new TerminalOperationTool();
        String result = tool.executeTerminalCommand("dir");
        Assertions.assertNotNull(result);
    }

}
