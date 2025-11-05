package com.deepseek.service.impl;

import com.deepseek.config.ModelConfig;
import com.deepseek.entity.AIRequest;
import com.deepseek.entity.AIResponse;
import com.deepseek.service.AIService;
import jakarta.annotation.Resource;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service("DeepSeekServiceImpl")
public class DeepSeekServiceImpl implements AIService {

    @Resource
    private OpenAiChatModel chatModel;

    @Resource
    private OllamaChatModel ollamaChatModel;

//    @Value("${deepseek.api.url}")
//    public String apiUrl;
//
//    @Value("${deepseek.api.key}")
//    public String apiKey;
//
//    private final RestTemplate restTemplate;
//
//    private HttpHeaders headers;
//
//    public DeepSeekServiceImpl() {
//        this.restTemplate = new RestTemplate();
//        this.headers = new HttpHeaders();
//        this.headers.set("Content-Type", "application/json");
//    }

    public Map callApi(Map req) {
        String model = req.get("model") != null ? req.get("model").toString() : "";
//        Optional<Object> op = Optional.ofNullable(req.get("model"));
//        String model = (String) op.orElse("");
        if (ModelConfig.LOCAL.equals(model)) {
            return callLocalApi(req);
        }

//        Object[] oa = null;
//        switch (model) {
//            case ModelConfig.DS_V3 -> oa = new Object[]{
//                    new HashMap<String, String>() {{
//                        put("role", "user");
//                        put("content", req.get("input").toString());
//                    }},
//                    new HashMap<String, String>() {{
//                        put("role", "system");
//                        put("content", req.get("systemInput") != null ? req.get("systemInput").toString() : "");
//                    }}};
//            case ModelConfig.DS_R1 -> oa = new Object[]{
//                    new HashMap<String, String>() {{
//                        put("role", "user");
//                        put("content", req.get("input").toString());
//                    }}
////                    new HashMap<String, String>() {{    // r1只支持role为user的请求，使用r1注释
////                        put("role", "system");
////                        put("content", input);
//            };
//            default -> oa = new Object[]{};
//        }
//
//        AIRequest request = new AIRequest(model, oa, false);
//        headers.set("Authorization", "Bearer " + apiKey);
//        HttpEntity<AIRequest> entity = new HttpEntity<>(request, headers);
//
//        Map result = new HashMap();
//        try {
//            ResponseEntity<AIResponse> response = restTemplate.postForEntity(apiUrl, entity, AIResponse.class);
//            result.put("code", "000");
//            result.put("output", response.getBody().message().get("content"));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            result.put("code", "400");
//            result.put("output", e.getMessage());
//        }
//        return result;

        Map result = new HashMap();
        try {
            result.put("code", "000");
            result.put("output", this.chatModel.call(req.get("input").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
        }
        return result;
    }

    public Map callLocalApi(Map req) {
//        Object[] oa = new Object[]{
//                new HashMap<String, String>() {{
//                    put("role", "user");
//                    put("content", req.get("input").toString());
//                }}
//        };
//        AIRequest request = new AIRequest(ModelConfig.DS_LOCAL_R1, oa, false);
//
//        HttpEntity<AIRequest> entity = new HttpEntity<>(request, headers);
//
//        Map result = new HashMap();
//        try {
//            ResponseEntity<AIResponse> response = restTemplate.postForEntity(ModelConfig.LOCAL_URL, entity, AIResponse.class);
//            result.put("code", "000");
//            result.put("output", response.getBody().message().get("content"));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            result.put("code", "400");
//            result.put("output", e.getMessage());
//        }
//        return result;

        Map result = new HashMap();
        try {
            result.put("code", "000");
            result.put("output", ollamaChatModel.call(req.get("input").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
        }
        return result;
    }

}
