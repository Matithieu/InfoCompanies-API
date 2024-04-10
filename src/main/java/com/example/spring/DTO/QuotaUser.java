package com.example.spring.DTO;

public enum QuotaUser {
    FREE, TIER1, TIER2, TIER3, ENTERPRISE, UNLIMITED;

    public int getRemainingSearchesBasedOnUserTier(User user) {
        return switch (user.getTier()) {
            case FREE -> 25;
            case TIER1 -> 100;
            case TIER2 -> 500;
            case TIER3 -> 1_000;
            case ENTERPRISE -> 10_000;
            case UNLIMITED -> Integer.MAX_VALUE;
        };
    }
}
