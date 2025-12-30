package com.example.spring.app.llm;

import com.example.spring.app.llm.dto.ChatStreamResponseDTO;
import com.example.spring.app.llm.dto.ConversationDTO;
import com.example.spring.app.llm.dto.MessageDTO;
import com.example.spring.app.llm.springAiChatMemory.SpringAiChatMemoryService;
import com.example.spring.app.llm.userConversation.UserConversationModel;
import com.example.spring.app.llm.userConversation.UserConversationService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;

import static com.example.spring.common.utils.JwtUtil.extractUserIdFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/v1/chat")
public class LLMController {

    private final ChatClient chatClient;
    private final UserConversationService userConversationService;
    private final SpringAiChatMemoryService springAiChatMemoryService;

    public LLMController(ChatClient chatClient, UserConversationService userConversationService, SpringAiChatMemoryService springAiChatMemoryService) {
        this.chatClient = chatClient;
        this.userConversationService = userConversationService;
        this.springAiChatMemoryService = springAiChatMemoryService;
    }

    @PostMapping(value = "/conversation/{conversationId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatStreamResponseDTO> streamGeneration(@PathVariable String conversationId, @RequestBody LLMRequest request) {
        String userId = extractUserIdFromHeader();

        UserConversationModel conversation =
                conversationId.equals("new")
                        ? userConversationService.createNewConversationForUser(userId)
                        : userConversationService.getUserConversation(conversationId, userId);

        if (conversation == null) {
            throw new RuntimeException("Conversation not found for user. Mismatched user or conversation ID.");
        }

        return chatClient.prompt()
                .user(userSpec -> userSpec.text(request.userInput()))
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversation.getConversationId()))
                .stream()
                .chatResponse()
                .map(chatResponse -> new ChatStreamResponseDTO(conversation.getConversationId(), chatResponse, Instant.now().toEpochMilli()));
    }


    // TODO: Add pagination to this endpoint
    @GetMapping("/conversation/history/{conversationId}")
    public List<MessageDTO> getConversationHistory(@PathVariable String conversationId) {
        String userId = extractUserIdFromHeader();
        UserConversationModel conversation = userConversationService.getUserConversation(conversationId, userId);

        if (conversation == null) {
            throw new RuntimeException("Conversation not found for user. Mismatched user or conversation ID.");
        }

        return springAiChatMemoryService.findAllByConversationId(conversationId).stream()
                .map(chatMemoryModel -> new MessageDTO(chatMemoryModel.getContent(), chatMemoryModel.getType(), chatMemoryModel.getTimestamp()))
                .toList();
    }

    // Issue with open-api generator which generates ENUM, and values are the sames
    @DeleteMapping("/conversation/delete/{conversationId}")
    public void deleteConversation(@PathVariable String conversationId) {
        String userId = extractUserIdFromHeader();
        springAiChatMemoryService.deleteAllByConversationId(conversationId);
        userConversationService.deleteUserConversation(conversationId, userId);
    }

    @GetMapping("/conversation/all")
    public List<ConversationDTO> getAllUserConversations() {
        String userId = extractUserIdFromHeader();
        List<UserConversationModel> conversations = userConversationService.getAllConversationsForUser(userId);

        return conversations.stream()
                .map(conv -> new ConversationDTO(conv.getConversationId()))
                .toList();
    }
}
