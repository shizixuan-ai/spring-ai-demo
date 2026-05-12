package com.example.spring_ai_demo.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.spring_ai_demo.agent.model.AgentState;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class BaseAgent {

    // 核心属性
    private String name;

    // 提示词
    private String systemPrompt;
    private String nextStepPrompt;

    // 代理状态
    private AgentState state = AgentState.IDLE;

    // 执行步骤控制
    private int currentStep = 0;
    private int maxSteps = 10;

    // LLM 大模型
    private ChatClient chatClient;

    // Memory 记忆（需要自主维护会话上下文）
    private List<Message> messageList = new ArrayList<>();

    public String run(String userPrompt) {
        // 基础校验
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state: " + this.state);
        }

        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent with empty user prompt");
        }
        // 状态更新
        this.state = AgentState.RUNNING;
        // 保存消息上下文
        this.messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> results = new ArrayList<>();
        try {
            // 循环执行
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing step {}/{}", stepNumber, maxSteps);
                // 单步执行
                String stepResult = step();
                String result = "Step " + stepNumber + ": " + stepResult;
                results.add(result);
            }
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }

            return String.join(",", results);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("error executing agent", e);
            this.state = AgentState.ERROR;
            return "执行错误" + e.getMessage();

        } finally {
            this.cleanup();
        }
    }

    /**
     * 运行代理（流式输出）
     * 
     * @param userPrompt
     * @return
     */
    public SseEmitter runStream(String userPrompt) {
        SseEmitter sseEmitter = new SseEmitter(300000L); // 5 分钟超时
        CompletableFuture.runAsync(() -> {
            try {
                // 基础校验
                if (this.state != AgentState.IDLE) {
                    sseEmitter.send("Cannot run agent from state: " + this.state);
                    sseEmitter.complete();
                    return;
                }

                if (StrUtil.isBlank(userPrompt)) {
                    sseEmitter.send("Cannot run agent with empty user prompt");
                    sseEmitter.complete();
                    return;
                }
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
            // 状态更新
            this.state = AgentState.RUNNING;
            // 保存消息上下文
            this.messageList.add(new UserMessage(userPrompt));
            // 保存结果列表
            List<String> results = new ArrayList<>();
            try {
                // 循环执行
                for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                    int stepNumber = i + 1;
                    currentStep = stepNumber;
                    log.info("Executing step {}/{}", stepNumber, maxSteps);
                    // 单步执行
                    String stepResult = step();
                    String result = "Step " + stepNumber + ": " + stepResult;
                    results.add(result);
                    // 输出当前每一步的结果到 SSE
                    sseEmitter.send(result);
                }
                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    String endResult = "Terminated: Reached max steps (" + maxSteps + ")";
                    results.add(endResult);
                    sseEmitter.send(endResult);
                }
                sseEmitter.complete();
                return;
            } catch (Exception e) {
                // TODO: handle exception
                log.error("error executing agent", e);
                this.state = AgentState.ERROR;
                try {
                    sseEmitter.send("error executing agent" + e.getMessage());
                    sseEmitter.complete();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    sseEmitter.completeWithError(e);
                }

            } finally {
                this.cleanup();
            }
        });
        // 设置超时回调
        sseEmitter.onTimeout(() -> {
            log.warn("SSE connection timeout");
            state = AgentState.ERROR;
            this.cleanup();
        });
        // 设置完成回调
        sseEmitter.onCompletion(() -> {
            log.info("SSE connection completed");
            if (state == AgentState.RUNNING) {
                state = AgentState.FINISHED;
            }
            this.cleanup();
        });
        return sseEmitter;
    }

    public abstract String step();

    public void cleanup() {

    }

}
