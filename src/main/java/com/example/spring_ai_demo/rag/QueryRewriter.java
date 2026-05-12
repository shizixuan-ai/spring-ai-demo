package com.example.spring_ai_demo.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

@Component
public class QueryRewriter {

    private final QueryTransformer queryTransformer;

    public QueryRewriter(ChatModel dashscopeChatModel) {
        Builder builder = ChatClient.builder(dashscopeChatModel);
        // 查询重写转换器
        queryTransformer = RewriteQueryTransformer.builder().chatClientBuilder(builder).build();
    }

    public String doRewriter(String query){
        return queryTransformer.transform(new Query(query)).text();
    }

}
