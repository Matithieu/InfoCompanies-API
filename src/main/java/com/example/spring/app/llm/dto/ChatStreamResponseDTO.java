package com.example.spring.app.llm.dto;

import org.springframework.ai.chat.model.ChatResponse;

public record ChatStreamResponseDTO(
    String conversationId,
    ChatResponse chatResponse,
    Long timestamp
){}
