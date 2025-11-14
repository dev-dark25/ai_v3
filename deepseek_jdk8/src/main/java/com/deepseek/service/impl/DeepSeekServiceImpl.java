package com.deepseek.service.impl;

import com.deepseek.config.ModelConfig;
import com.deepseek.entity.AIRequest;
import com.deepseek.entity.AIResponse;
import com.deepseek.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service("DeepSeekServiceImpl")
public class DeepSeekServiceImpl implements AIService {

    @Value("${deepseek.api.url}")
    public String apiUrl;

    @Value("${deepseek.api.key}")
    public String apiKey;

    private final RestTemplate restTemplate;

    private HttpHeaders headers;

    public DeepSeekServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.set("Content-Type", "application/json");
    }

    public Map callApi(Map req) {
        String model = req.get("model").toString();
        if (ModelConfig.LOCAL.equals(model)) {
            return callLocalApi(req);
        }

        AIRequest request = new AIRequest();
        request.setModel(ModelConfig.DS_V3.equals(model) || ModelConfig.DS_R1.equals(model) ? model : ModelConfig.DS_R1);

        Object[] oa = null;
        if (ModelConfig.DS_V3.equals(model)) {
            oa = new Object[]{
                    new HashMap<String, String>() {{
                        put("role", "user");
                        put("content", req.get("input").toString());
                    }},
                    new HashMap<String, String>() {{
                        put("role", "system");
                        put("content", req.get("systemInput") != null ? req.get("systemInput").toString() : "");
                    }}};
        } else if (ModelConfig.DS_R1.equals(model)) {
            oa = new Object[]{
                    new HashMap<String, String>() {{
                        put("role", "user");
                        put("content", req.get("input").toString());
                    }}
//                    new HashMap<String, String>() {{    // r1只支持role为user的请求，使用r1注释
//                        put("role", "system");
//                        put("content", input);
            };
        } else {
            oa = new Object[]{};
        }

        request.setMessages(oa);
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<AIRequest> entity = new HttpEntity<>(request, headers);

        Map result = new HashMap();
        try {
            ResponseEntity<AIResponse> response = restTemplate.postForEntity(apiUrl, entity, AIResponse.class);
            result.put("code", "000");
            result.put("output", response.getBody().getMessage().get("content"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
        }
        return result;
    }

    public Map callLocalApi(Map req) {
        AIRequest request = new AIRequest();
        request.setModel(ModelConfig.DS_LOCAL_R1);
        request.setStream(false);

        Object[] oa = new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", req.get("input").toString());
                }}
        };
        request.setMessages(oa);
        HttpEntity<AIRequest> entity = new HttpEntity<>(request, headers);

        Map result = new HashMap();
        try {
            ResponseEntity<AIResponse> response = restTemplate.postForEntity(ModelConfig.LOCAL_URL, entity, AIResponse.class);
            result.put("code", "000");
            result.put("output", response.getBody().getMessage().get("content"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
        }
        return result;
    }

}
