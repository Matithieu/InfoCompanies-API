package com.example.spring.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserQuota {
    // Getters and setters
    @Id
    private String userId;
    private int quotaAllocated;
    private int quotaUsed;
}
