package com.example.spring_ai_demo.application;

import java.util.List;

import com.example.spring_ai_demo.advisor.LoggerAdvisor;
import com.example.spring_ai_demo.rag.LoveAppRagCustomAdvisorFactory;
import com.example.spring_ai_demo.rag.QueryRewriter;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoveAPP {

        @Autowired
        private QueryRewriter queryRewriter;

        private final ChatClient client;

        private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
                        "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
                        "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
                        "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";
        @Autowired
        private Advisor LoveAppRagCloudAdvisor;

        @Autowired
        private VectorStore loveAppVectorStore;

        @Autowired
        private VectorStore pgVectorVectorStore;

        @Autowired
        private ToolCallback[] tools;

        @Autowired
        private ToolCallbackProvider toolCallbackProvider;

        public LoveAPP(@Qualifier("dashscopeChatModel") ChatModel chatModel) { // 本地模型：ollamaChatModel
                // 基于文本对话记录
                // String baseDir = System.getProperty("user.dir") + "/tmp/chat_memory";
                // ChatMemory chatMemory = new FileBasedChatMemory(baseDir);
                // 基于内存对话记录
                ChatMemory chatMemory = MessageWindowChatMemory.builder()
                                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                                .maxMessages(15)
                                .build();
                client = ChatClient.builder(chatModel)
                                .defaultSystem(SYSTEM_PROMPT).defaultAdvisors(
                                                MessageChatMemoryAdvisor.builder(chatMemory).build(),
                                                new LoggerAdvisor())
                                .build();
        }

        public String doChat(String message, String chatId) {
                ChatResponse chatResponse = client.prompt().user(message).advisors(
                                spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                // .param(VectorStoreChatMemoryAdvisor.TOP_K, 10)
                )
                                .call().chatResponse();
                String response = chatResponse.getResult().getOutput().getText();
                log.info("AI response = {}", response);
                return response;
        }

        record LoveReport(String title, List<String> suggestions) {

        }

        public LoveReport doChatWithReport(String message, String chatId) {
                LoveReport response = client.prompt()
                                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                                .user(message)
                                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                                // .param(VectorStoreChatMemoryAdvisor.TOP_K, 10)
                                )
                                .call().entity(LoveReport.class);
                log.info("AI response = {}", response);
                return response;
        }

        public String doChatWithRAG(String message, String chatId) {
                String rewrittenMessage = queryRewriter.doRewriter(message);
                ChatResponse chatResponse = client.prompt()
                                .user(message)
                                .user(rewrittenMessage)// 使用改写后的查询
                                .advisors(
                                                spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                                                                .param(VectorStoreChatMemoryAdvisor.TOP_K, 10))
                                .advisors(new LoggerAdvisor())
                                // .advisors(new QuestionAnswerAdvisor(LoveAppVectorStore))
                                // .advisors(new QuestionAnswerAdvisor(LoveAppVectorStore))
                                .advisors(LoveAppRagCloudAdvisor)
                                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                                .advisors(LoveAppRagCustomAdvisorFactory
                                                .createLoveAppRagCustomAdvisor(loveAppVectorStore, "单身"))
                                .call().chatResponse();
                String response = chatResponse.getResult().getOutput().getText();
                log.info("AI ARG response = {}", response);
                return response;
        }

        public String doChatWithTools(String message, String chatId) {

                ChatResponse chatResponse = client
                                .prompt()
                                .user(message)
                                .advisors(
                                                spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                                                                .param(VectorStoreChatMemoryAdvisor.TOP_K, 10))
                                .advisors(new LoggerAdvisor())
                                .tools(tools)
                                .call()
                                .chatResponse();
                String response = chatResponse.getResult().getOutput().getText();
                log.info("AI response = {}", response);
                return response;
        }

        public String doChatWithMCP(String message, String chatId) {
                ChatResponse chatResponse = client
                                .prompt()
                                .user(message)
                                .advisors(
                                                spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                                                                .param(VectorStoreChatMemoryAdvisor.TOP_K, 10))
                                                                .advisors(new LoggerAdvisor())
                                                                .tools(toolCallbackProvider)
                                .call()
                                .chatResponse();
                String response = chatResponse.getResult().getOutput().getText();
                log.info("AI response = {}", response);
                return response;
        }

}
