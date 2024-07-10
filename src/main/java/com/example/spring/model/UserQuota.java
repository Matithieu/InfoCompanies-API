package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class UserQuota {

    // Getters and setters
    @Id
    private String userId;
    private int quotaAllocated;
    private int quotaUsed;
    private LocalDate lastResetDate; // Add this field

    // Constructors, getters, and setters

    public UserQuota() {}

    public UserQuota(String userId, int quotaAllocated, int quotaUsed) {
        this.userId = userId;
        this.quotaAllocated = quotaAllocated;
        this.quotaUsed = quotaUsed;
        this.lastResetDate = LocalDate.now(); // Initialize with current date
    }
}
