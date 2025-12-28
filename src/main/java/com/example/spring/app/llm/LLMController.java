package com.example.spring.app.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
@RequestMapping("/v1")
public class LLMController {

    private final ChatClient chatClient;

    public LLMController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ask-ai")
    LLMAnswerDTO generation(String userInput) {
        LLMAnswerDTO llmAnswerDTO = new LLMAnswerDTO();
        String response = this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();

        llmAnswerDTO.setAnswer(response);
        return llmAnswerDTO;
    }

    @GetMapping(value = "/stream-ai", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> streamGeneration(@RequestParam String userInput) {
        return chatClient.prompt()
                .user(userInput)
                .stream()
                .chatResponse();
    }
}
