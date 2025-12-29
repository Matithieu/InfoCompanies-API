package com.example.spring.app.llm.dto;

import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;

public record MessageDTO(
    String message,
    MessageType messageType,
    LocalDateTime timestamp
) {}
