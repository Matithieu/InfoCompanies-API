package com.example.spring.core.userQuota;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_quota")
public class UserQuotaModel {

    // Getters and setters
    @Id
    private String userId;
    private Integer quotaAllocated;
    private Integer quotaUsed;

    public UserQuotaModel() {}

    public UserQuotaModel(String userId, Integer quotaAllocated, Integer quotaUsed) {
        this.userId = userId;
        this.quotaAllocated = quotaAllocated;
        this.quotaUsed = quotaUsed;
    }
}
