package com.deepseek.service.impl;

import com.deepseek.config.ModelConfig;
import com.deepseek.service.AIService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("VolcengineServiceImpl")
public class VolcengineServiceImpl implements AIService {

    @Value("${volcengine.api.url}")
    public String apiUrl;

    @Value("${volcengine.api.key}")
    public String apiKey;

    public Map callApi(Map req) {
        // 初始化消息列表
        List<ChatMessage> chatMessages = new ArrayList<>();

        // 创建用户消息
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(req.get("input").toString()) // 设置消息内容
                .build();

        // 将用户消息添加到消息列表
        chatMessages.add(userMessage);

        // 创建聊天完成请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(ModelConfig.VE_S)
                .messages(chatMessages) // 设置消息列表
                .build();

        ArkService arkService = ArkService.builder().apiKey(apiKey).baseUrl(apiUrl).build();

        Map result = new HashMap();
        // 发送聊天完成请求并打印响应
        try {
            // 获取响应并打印每个选择的消息内容
//            arkService.createChatCompletion(chatCompletionRequest)
//                    .getChoices()
//                    .forEach(choice -> System.out.println(choice.getMessage().getContent()));
            result.put("code", "000");
            result.put("output", arkService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
        } finally {
            // 关闭服务执行器
            arkService.shutdownExecutor();
        }
        return result;
    }

    public Map callLocalApi(Map req) {
        return null;
    }

}
