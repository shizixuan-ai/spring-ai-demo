package com.example.spring_ai_demo.config;

import com.example.spring_ai_demo.tools.FileOperationToolTest;
import com.example.spring_ai_demo.tools.PDFGenerationToolTest;
import com.example.spring_ai_demo.tools.ResourceDownloadToolTest;
import com.example.spring_ai_demo.tools.TerminalOperationToolTest;
import com.example.spring_ai_demo.tools.TerminateTool;
import com.example.spring_ai_demo.tools.WebScrapingToolTest;
import com.example.spring_ai_demo.tools.WebSearchToolTest;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String webSerchToolApiKey;

    @Bean
    public ToolCallback[] getToolCallbacks() {
        FileOperationToolTest fileOperationTool = new FileOperationToolTest();
        PDFGenerationToolTest pdfGenerationTool = new PDFGenerationToolTest();
        ResourceDownloadToolTest resourceDownloadTool = new ResourceDownloadToolTest();
        TerminalOperationToolTest terminalOperationTool = new TerminalOperationToolTest();
        WebScrapingToolTest webScrapingTool = new WebScrapingToolTest();
        WebSearchToolTest webSearchTool = new WebSearchToolTest(webSerchToolApiKey);
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(fileOperationTool, pdfGenerationTool, resourceDownloadTool, terminalOperationTool,
                webScrapingTool, webSearchTool, terminateTool);
    }

}
