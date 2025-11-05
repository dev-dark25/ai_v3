package com.deepseek.service;

import java.util.Map;

public interface AIService {

    Map callApi(Map req);

    Map callLocalApi(Map req);
}
