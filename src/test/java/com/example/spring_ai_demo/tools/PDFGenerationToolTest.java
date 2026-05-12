package com.example.spring_ai_demo.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF(){
        PDFGenerationTool tool = new PDFGenerationTool();
        String result = tool.generatePDF("test.pdf", "test123");
        Assertions.assertNotNull(result);
    }
}
