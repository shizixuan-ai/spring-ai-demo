package com.example.spring_ai_demo.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class WebSearchTool {

    // SearchAPI 的搜索接口地址
    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    private final String apikey;
    
    

    public WebSearchTool(String apikey) {
        this.apikey = apikey;
    }



    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(@ToolParam(description = "Search query keyword") String query) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apikey);
        paramMap.put("engine", "baidu");

        try {
            String response = HttpUtil.get(SEARCH_API_URL, paramMap);
            JSONObject obj = JSONUtil.parseObj(response);
            JSONArray jsonArray = obj.getJSONArray("organic_results");
            // 取出返回结果的前 5 条
            List<Object> subList = jsonArray.subList(0, 5);
            String result = subList.stream().map(object ->{
                JSONObject jsonObject = (JSONObject)object;
                return jsonObject.toString();
            }).collect(Collectors.joining(","));

            return result;
        } catch (Exception e) {
            // TODO: handle exception
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}
