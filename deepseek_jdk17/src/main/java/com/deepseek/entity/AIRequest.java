package com.deepseek.entity;

public record AIRequest(String model, Object[] messages, boolean stream) {
}
