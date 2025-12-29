package com.example.spring.app.llm.springAiChatMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringAiChatMemoryService {
    @Autowired
    private SpringAiChatMemoryRepository userConversationRepository;

    public List<SpringAiChatMemoryModel> findAllByConversationId(String conversationId) {
        return userConversationRepository.findAllByConversationId(conversationId);
    }
}
