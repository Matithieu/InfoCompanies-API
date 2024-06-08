package com.example.spring.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserQuota {
    @Id
    private String userId;
    private int quotaAllocated;
    private int quotaUsed;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getQuotaAllocated() {
        return quotaAllocated;
    }

    public void setQuotaAllocated(int quotaAllocated) {
        this.quotaAllocated = quotaAllocated;
    }

    public int getQuotaUsed() {
        return quotaUsed;
    }

    public void setQuotaUsed(int quotaUsed) {
        this.quotaUsed = quotaUsed;
    }
}
