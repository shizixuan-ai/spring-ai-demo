package com.example.spring_ai_demo.llminvoke;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SDKInvoke {

    @Test
    public void testSDKInvoke(String[] args) {
        // Generation gen = new Generation();

        // Message systemMessage = Message.builder().role(Message.Role.SYSTEM).content("You are a helpful assistant.").build();

        // Message userMessage = Message.builder().role(Message.Role.USER).content("Hello, how are you?").build();

        // GeneralGetParam param = GeneralGetParam.builder().apiKey(BaseConta.API_KEY).model("qwen-plus").messages(Arrays.asList(userMessage, systemMessage)).resultFormat(GeneralGetParam.ResultFormat.MESSAGE).build();
        // GenerationResult response = gen.call(param);
        // System.out.println(JSONUtil.toJsonStr(response));
    }
}