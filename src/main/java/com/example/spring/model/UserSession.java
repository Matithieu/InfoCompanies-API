package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@Component
@Scope("session")
@Entity
@Table(name = "userssessions")
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sessionID")
    private String sessionId;

    public UserSession() {
    }

    public UserSession(Long id, String sessionId) {
        this.id = id;
        this.sessionId = sessionId;
    }

    public Long createId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    public String createSessionId() {
        return UUID.randomUUID().toString();
    }
}
