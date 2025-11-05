package com.deepseek.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.deepseek.config.ModelConfig;
import com.deepseek.service.AIService;
import jakarta.annotation.Resource;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class QWenServiceImpl implements AIService {

    @Resource
    private OllamaChatModel ollamaChatModel;

    @Value("${aliyun.api.key}")
    public String apiKey;

    public Map callApi(Map req) {
        String model = req.get("model") != null ? req.get("model").toString() : "";
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
        }
        return result;
    }

    public Map callLocalApi(Map req) {
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