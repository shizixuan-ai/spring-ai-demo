package com.example.spring_ai_demo.config;

import java.util.List;

import com.example.spring_ai_demo.rag.LoveAppDocumentLoader;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class PgVectorVectorStoreConfig {

    @Autowired
    private LoveAppDocumentLoader loader;
    
    @Bean
    public VectorStore getPgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel){
        PgVectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
        .dimensions(1536)
        .distanceType(PgDistanceType.COSINE_DISTANCE)
        .indexType(PgIndexType.HNSW).initializeSchema(true)
        .schemaName("public")
        .vectorTableName("vector_store")
        .maxDocumentBatchSize(10000)
        .build();
        //加载文档
        List<Document> docs = loader.loadMarkdowns();
        vectorStore.add(docs);
        return vectorStore;
    }
}
