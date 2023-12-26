package com.example.spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userssessions")
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sessionID")
    private String session_id;

    public UserSession() {}

    public UserSession(String session_id ) {
        super();
        this.session_id = session_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
