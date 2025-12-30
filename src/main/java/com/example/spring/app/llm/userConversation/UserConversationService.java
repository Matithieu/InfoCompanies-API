package com.example.spring.app.llm.userConversation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserConversationService {
    @Autowired
    private UserConversationRepository userConversationRepository;

    public UserConversationModel getUserConversation(String conversationId, String userId) {
        return userConversationRepository.findByConversationIdAndUserId(conversationId, userId);
    }

    public UserConversationModel createNewConversationForUser(String userId) {
        UserConversationModel newConversation = new UserConversationModel();
        newConversation.setUserId(userId);
        String conversationId = UUID.randomUUID().toString();
        newConversation.setConversationId(conversationId);
        return userConversationRepository.save(newConversation);
    }

    public List<UserConversationModel> getAllConversationsForUser(String userId) {
        return userConversationRepository.findAllByUserId(userId);
    }

    public void deleteUserConversation(String conversationId, String userId) {
        UserConversationModel conversation = getUserConversation(conversationId, userId);
        if (conversation != null) {
            userConversationRepository.delete(conversation);
            return;
        }

        throw new RuntimeException("Conversation not found for user. Mismatched user or conversation ID.");
    }
}
