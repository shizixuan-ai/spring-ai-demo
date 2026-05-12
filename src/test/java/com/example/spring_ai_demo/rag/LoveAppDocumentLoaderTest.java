package com.example.spring_ai_demo.rag;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoveAppDocumentLoaderTest {

    @Autowired
    private LoveAppDocumentLoader loader;
    
    @Test
    public void testLoadMarkdowns(){
        List<Document> markdowns = loader.loadMarkdowns();

    }
}
