package com.example.spring.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignComparator {
    LOWER_THAN("<"),
    GREATER_THAN(">"),
    EQUAL("=");

    private final String symbol;

    SignComparator(String symbol) {
        this.symbol = symbol;
    }

    @JsonValue
    public String getSymbol() {
        return symbol;
    }

    @JsonCreator
    public static SignComparator fromValue(String value) {
        for (SignComparator c : values()) {
            if (c.symbol.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid comparator: " + value);
    }
}
