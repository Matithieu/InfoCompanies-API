package com.example.spring.core.userQuota;

import com.example.spring.common.enums.TierUser;
import com.example.spring.app.user.UserDTO;
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
        priceIdToTierMap.put("ENTERPRISE", "ENTERPRISE");
        priceIdToTierMap.put("UNLIMITED", "UNLIMITED");
    }

    public static TierUser getQuotaBasedOnTier(String tier) {
        return switch (tier) {
            case "TIER1" -> TierUser.TIER1;
            case "TIER2" -> TierUser.TIER2;
            case "ENTERPRISE" -> TierUser.ENTERPRISE;
            case "UNLIMITED" -> TierUser.UNLIMITED;

            case null, default -> TierUser.FREE;
        };
    }

    public static String getTierBasedOnPriceId(String priceId) {
        if (priceIdToTierMap.containsKey(priceId)) {
            return priceIdToTierMap.get(priceId);
        } else {
            throw new IllegalStateException("Unexpected value: " + priceId);
        }
    }

    public static Integer getRemainingSearchesBasedOnUserTier(UserDTO user) {
        return switch (user.getTier()) {
            case FREE -> 15;
            case TIER1 -> 100;
            case TIER2 -> 200;
            case ENTERPRISE -> 10_000;
            case UNLIMITED -> Integer.MAX_VALUE;
        };
    }
}
