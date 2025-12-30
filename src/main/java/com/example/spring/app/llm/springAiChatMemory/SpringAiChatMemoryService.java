package com.example.spring.app.llm.springAiChatMemory;

import jakarta.transaction.Transactional;
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

    @Transactional
    public void deleteAllByConversationId(String conversationId) {
        userConversationRepository.deleteAllByConversationId(conversationId);
    }
}
