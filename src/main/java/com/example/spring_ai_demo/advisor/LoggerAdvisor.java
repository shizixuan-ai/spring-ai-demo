package com.example.spring_ai_demo.advisor;

import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class LoggerAdvisor implements CallAdvisor,StreamAdvisor {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        // TODO Auto-generated method stub
        chatClientRequest = before(chatClientRequest);
        ChatClientResponse nextCall = callAdvisorChain.nextCall(chatClientRequest);
        after(nextCall);
        return nextCall;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
            StreamAdvisorChain streamAdvisorChain) {
        // TODO Auto-generated method stub
        chatClientRequest = before(chatClientRequest);
        Flux<ChatClientResponse> nextStream = streamAdvisorChain.nextStream(chatClientRequest);
        return (new ChatClientMessageAggregator()).aggregateChatClientResponse(nextStream,this::after);
    }

    private ChatClientRequest before(ChatClientRequest chatClientRequest) {
        // TODO Auto-generated method stub
        log.info("AI request={}",chatClientRequest.prompt());
        return chatClientRequest;
    }

    private void after(ChatClientResponse nextCall) {
        // TODO Auto-generated method stub
        log.info("AI response={}",nextCall.chatResponse().getResult().getOutput().getText());
    }

    
    
}
