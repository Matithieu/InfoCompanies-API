package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserQuota {

    // Getters and setters
    @Id
    private String userId;
    private Integer quotaAllocated;
    private Integer quotaUsed;

    public UserQuota() {}

    public UserQuota(String userId, Integer quotaAllocated, Integer quotaUsed) {
        this.userId = userId;
        this.quotaAllocated = quotaAllocated;
        this.quotaUsed = quotaUsed;
    }
}
