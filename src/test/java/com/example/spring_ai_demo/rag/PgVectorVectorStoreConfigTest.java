package com.example.spring_ai_demo.rag;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class PgVectorVectorStoreConfigTest {

    @Autowired
    private VectorStore pgVectorVectorStore;

    @Test
    public void testPgVectorVectorStore(){
        List<Document> documents = List.of(
            new Document("鱼皮的编程导航有什么用？学编程啊，做项目啊", Map.of("meta1", "meta1")),
            new Document("程序员鱼皮的原创项目教程 codefather.cn"),
            new Document("鱼皮这小伙子比较帅气", Map.of("meta2", "meta2")));

        pgVectorVectorStore.add(documents);

        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("怎么学编程").similarityThreshold(0.8).topK(3).build());
        Assertions.assertNotNull(results);
    }

}
