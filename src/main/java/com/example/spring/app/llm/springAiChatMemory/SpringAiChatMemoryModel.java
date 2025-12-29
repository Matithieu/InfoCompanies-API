package com.example.spring.app.llm.springAiChatMemory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "spring_ai_chat_memory")
public class SpringAiChatMemoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Length(max = 36)
    private String conversationId;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private MessageType type;

    private String content;
    private LocalDateTime timestamp;
}
