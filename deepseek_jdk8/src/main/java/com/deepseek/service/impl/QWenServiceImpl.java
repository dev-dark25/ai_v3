package com.deepseek.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.deepseek.config.ModelConfig;
import com.deepseek.entity.AIResponse;
import com.deepseek.service.AIService;
import com.deepseek.entity.AIRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class QWenServiceImpl implements AIService {

    @Value("${aliyun.api.key}")
    public String apiKey;

    private final RestTemplate restTemplate;

    private HttpHeaders headers;

    public QWenServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.set("Content-Type", "application/json");
    }

    public Map callApi(Map req) {
        String model = req.get("model").toString();
        if (ModelConfig.LOCAL.equals(model)) {
            return callLocalApi(req);
        }

        Generation gen = new Generation();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(req.get("input").toString())
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用阿里云百炼API Key将下行替换为：.apiKey("sk-xxx")
//                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .apiKey(apiKey)
                // 模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model(ModelConfig.QW_P)
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        Map result = new HashMap();
        try {
            GenerationResult gr = gen.call(param);
            result.put("code", "000");
            result.put("output", gr.getOutput().getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
            System.out.println("请参考文档：https://help.aliyun.com/zh/model-studio/developer-reference/error-code");
        }
        return result;
    }

    public Map callLocalApi(Map req) {
        AIRequest request = new AIRequest();
        request.setModel(ModelConfig.QW_LOCAL_QW3);
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