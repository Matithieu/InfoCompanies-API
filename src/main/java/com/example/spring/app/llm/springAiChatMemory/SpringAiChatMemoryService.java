package com.example.spring.app.llm.springAiChatMemory;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringAiChatMemoryService {

    private final SpringAiChatMemoryRepository userConversationRepository;

    public SpringAiChatMemoryService(SpringAiChatMemoryRepository userConversationRepository) {
        this.userConversationRepository = userConversationRepository;
    }

    public List<SpringAiChatMemoryModel> findAllByConversationId(String conversationId) {
        return userConversationRepository.findAllByConversationId(conversationId);
    }

    @Transactional
    public void deleteAllByConversationId(String conversationId) {
        userConversationRepository.deleteAllByConversationId(conversationId);
    }
}
