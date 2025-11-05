package com.deepseek.controller;

import com.deepseek.config.ModelConfig;
import com.deepseek.service.AIService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class WebController {

    @Autowired
    @Qualifier("DeepSeekServiceImpl")
    private AIService deepSeekService;

    @Resource
    @Qualifier("QWenServiceImpl")
    private AIService qwenService;

    @PostMapping("/call")
    public Map callApi(@RequestBody Map<String, Object> req) {
        System.out.println("input: " + req.get("input"));
        System.out.println("model: " + req.get("model"));
        System.out.println("type: " + req.get("type"));
        String type = (String) req.get("type");
        Map result;
        switch (type) {
            case ModelConfig.DS_NAME:
                result = deepSeekService.callApi(req);
                break;
            case ModelConfig.QW_NAME:
                result = qwenService.callApi(req);
                break;
            default:
                result = null;
        }
        System.out.println(result);
        return result;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Map test(@RequestBody String body) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "200");
        return map;
    }

}
