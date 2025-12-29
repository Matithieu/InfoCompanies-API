package com.example.spring.app.llm.userConversation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserConversationRepository extends JpaRepository<UserConversationModel, Integer> {
    UserConversationModel findByConversationIdAndUserId(String conversationId, String userId);
    List<UserConversationModel> findAllByUserId(String userId);
}
