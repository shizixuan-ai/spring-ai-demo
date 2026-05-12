package com.example.spring_ai_demo.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;

import reactor.core.publisher.Flux;

public class ReReadingAdvisor implements CallAdvisor, StreamAdvisor {

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
        return callAdvisorChain.nextCall(before(chatClientRequest));
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
            StreamAdvisorChain streamAdvisorChain) {
        // TODO Auto-generated method stub
        return streamAdvisorChain.nextStream(before(chatClientRequest));
    }

    private ChatClientRequest before(ChatClientRequest chatClientRequest) {
        // TODO Auto-generated method stub

        String userText = chatClientRequest.prompt().getUserMessage().getText();

        chatClientRequest.context().put("re2_input_query", userText);

        String newUserText = """
                %s
                Read the question again: %s
                """.formatted(userText, userText);

        Prompt userPrompt = chatClientRequest.prompt().augmentUserMessage(newUserText);

        return new ChatClientRequest(userPrompt, chatClientRequest.context());

    }

}
