package com.deepseek.entity;

import lombok.Data;

@Data
public class AIRequest {
    private String model;
    private Object[] messages;
    private boolean stream;
}
