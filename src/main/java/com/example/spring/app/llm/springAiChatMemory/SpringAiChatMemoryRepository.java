package com.example.spring.app.llm.springAiChatMemory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringAiChatMemoryRepository extends JpaRepository<SpringAiChatMemoryModel, Integer> {
    List<SpringAiChatMemoryModel> findAllByConversationId(String conversationId);
}
