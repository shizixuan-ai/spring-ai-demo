package com.example.spring_ai_demo.config;

import java.util.List;

import com.example.spring_ai_demo.rag.LoveAppDocumentLoader;
import com.example.spring_ai_demo.rag.MyKeywordEnricher;
import com.example.spring_ai_demo.rag.MyTokenTextSplitter;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loader;

    @Resource
    private MyTokenTextSplitter tokenTextSplitter;

    @Resource
    private MyKeywordEnricher keywordEnricher;

    @Bean
    public VectorStore getLoveAPPVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documentList = loader.loadMarkdowns();
        // 自主切分文档
        // List<Document> splitCustomized =
        // tokenTextSplitter.splitCustomized(documentList);
        // 自动补充关键词元信息
        List<Document> enrichDocuments = keywordEnricher.enrichDocuments(documentList);
        simpleVectorStore.add(enrichDocuments);
        return simpleVectorStore;
    }
}
