package com.example.spring.utils;

import com.example.spring.DTO.QuotaUser;

public class UserQuotaUtil {

    public static QuotaUser getQuotaBasedOnTier(String tier) {
        return switch (tier) {
            case "TIER1" -> QuotaUser.TIER1;
            case "TIER2" -> QuotaUser.TIER2;
            case "ENTERPRISE" -> QuotaUser.ENTERPRISE;
            case "UNLIMITED" -> QuotaUser.UNLIMITED;

            case null, default -> QuotaUser.FREE;
        };
    }

    // Replace this by the Price ID of stuff
    public static String getTierBasedOnPriceId(String priceId) {
        return switch (priceId) {
            case "FREE" -> "TIER1";
            case "TIER2" -> "TIER2";
            case "UNLIMITED" -> "UNLIMITED";
            case "ENTERPRISE" -> "ENTERPRISE";

            default -> throw new IllegalStateException("Unexpected value: " + priceId);
        };
    }
}
