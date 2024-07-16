package com.example.spring.utils;

import com.example.spring.DTO.QuotaUser;
import com.example.spring.DTO.User;
import com.example.spring.config.StripeConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserQuotaUtil {

    private static StripeConfig stripeConfig;

    private static Map<String, String> priceIdToTierMap;

    @Autowired
    public UserQuotaUtil(StripeConfig stripeConfig) {
        UserQuotaUtil.stripeConfig = stripeConfig;
    }

    @PostConstruct
    public void init() {
        priceIdToTierMap = new HashMap<>();
        priceIdToTierMap.put(stripeConfig.getStripePriceIdFree(), "FREE");
        priceIdToTierMap.put(stripeConfig.getStripePriceIdBasic(), "TIER1");
        priceIdToTierMap.put(stripeConfig.getStripePriceIdPremium(), "TIER2");
        priceIdToTierMap.put("UNLIMITED", "UNLIMITED");
        priceIdToTierMap.put("ENTERPRISE", "ENTERPRISE");
    }

    public static QuotaUser getQuotaBasedOnTier(String tier) {
        return switch (tier) {
            case "TIER1" -> QuotaUser.TIER1;
            case "TIER2" -> QuotaUser.TIER2;
            case "ENTERPRISE" -> QuotaUser.ENTERPRISE;
            case "UNLIMITED" -> QuotaUser.UNLIMITED;

            case null, default -> QuotaUser.FREE;
        };
    }

    public static String getTierBasedOnPriceId(String priceId) {
        if (priceIdToTierMap.containsKey(priceId)) {
            return priceIdToTierMap.get(priceId);
        } else {
            throw new IllegalStateException("Unexpected value: " + priceId);
        }
    }

    public static int getRemainingSearchesBasedOnUserTier(User user) {
        return switch (user.getTier()) {
            case FREE -> 20;
            case TIER1 -> 100;
            case TIER2 -> 500;
            case ENTERPRISE -> 10_000;
            case UNLIMITED -> Integer.MAX_VALUE;
        };
    }
}
