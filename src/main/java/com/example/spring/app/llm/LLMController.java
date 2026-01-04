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

import static com.example.spring.app.llm.LLMUtils.wrapUserInputWithConversationContext;
import static com.example.spring.common.utils.JwtUtil.extractUserIdFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/v1/conversations")
public class LLMController {

    private final ChatClient titleClient;
    private final ChatClient chatClient;
    private final UserConversationService userConversationService;
    private final SpringAiChatMemoryService springAiChatMemoryService;

    public LLMController(ChatClient titleClient, ChatClient chatClient, UserConversationService userConversationService, SpringAiChatMemoryService springAiChatMemoryService) {
        this.titleClient = titleClient;
        this.chatClient = chatClient;
        this.userConversationService = userConversationService;
        this.springAiChatMemoryService = springAiChatMemoryService;
    }

    @GetMapping("/")
    public List<ConversationDTO> getAllUserConversations() {
        String userId = extractUserIdFromHeader();
        List<UserConversationModel> conversations = userConversationService.getAllConversationsForUser(userId);

        return conversations.stream()
                .map(conv -> new ConversationDTO(conv.getConversationId(), conv.getTitle()))
                .toList();
    }

    @GetMapping("/{conversationId}")
    public ConversationDTO getSingleConversation(@PathVariable String conversationId) {
        String userId = extractUserIdFromHeader();
        UserConversationModel conversation = userConversationService.getUserConversation(conversationId, userId);
        if (conversation == null) {
            throw new RuntimeException("Conversation not found for user. Mismatched user or conversation ID.");
        }

        return new ConversationDTO(conversation.getConversationId(), conversation.getTitle());
    }

    // TODO: Add pagination to this endpoint
    @GetMapping("/{conversationId}/messages")
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

    @DeleteMapping("/{conversationId}")
    public void deleteConversation(@PathVariable String conversationId) {
        String userId = extractUserIdFromHeader();
        springAiChatMemoryService.deleteAllByConversationId(conversationId);
        userConversationService.deleteUserConversation(conversationId, userId);
    }

    @PostMapping(value = "/{conversationId}/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatStreamResponseDTO> streamGeneration(@PathVariable String conversationId, @RequestBody LLMRequest request) {
        String userId = extractUserIdFromHeader();
        String userInput = wrapUserInputWithConversationContext(request.userInput());
        boolean isNewConversation = conversationId.equals("new");
        String conversationTitle = isNewConversation
                ? titleClient.prompt().user(userInput).call().content()
                : null;

        UserConversationModel conversation =
                isNewConversation
                        ? userConversationService.createNewConversationForUser(userId, conversationTitle)
                        : userConversationService.getUserConversation(conversationId, userId);

        if (conversation == null) {
            throw new RuntimeException("Conversation not found for user. Mismatched user or conversation ID.");
        }

        return chatClient.prompt()
                .user(userSpec -> userSpec.text(userInput))
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversation.getConversationId()))
                .stream()
                .chatResponse()
                .map(chatResponse -> new ChatStreamResponseDTO(conversation.getConversationId(), chatResponse, Instant.now().toEpochMilli()));
    }
}
