package com.example.spring_ai_demo.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

/**
 * 自定义基于 Token 的切词器
 */
public class MyTokenTextSplitter {
    public List<Document> splitDocuments(List<Document> docs){
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        return tokenTextSplitter.apply(docs);

    }

    public List<Document> splitCustomized(List<Document> docs){
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(200,100,10,5000,true);
        return tokenTextSplitter.apply(docs);
    }
}
