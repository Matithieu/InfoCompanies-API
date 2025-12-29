package com.example.spring.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

import static com.example.spring.common.utils.HeadersUtil.parseTokenFromHeader;

public class JwtUtil {
    public static String[] splitToken(String token) {
        return token.split("\\.");
    }

    public static String decodePayload(String token) {
        String[] parts = splitToken(token);
        return new String(Base64.getDecoder().decode(parts[1]));
    }

    public static String extractUserIdFromHeader() {
        String token = parseTokenFromHeader();
        String payload = decodePayload(token);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode node = jsonNode.path("sub");
        return node.asText();
    }
}
