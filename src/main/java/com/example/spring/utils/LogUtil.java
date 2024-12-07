package com.example.spring.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void info(String message, Map<String, Object> context) {
        logger.info("{} {}", message, formatContext(context));
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    private static String formatContext(Map<String, Object> context) {
        return context.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
    }

    /**
     * Determines whether to log based on a given scale.
     * @param scale the probability (0.0 to 1.0) of logging.
     * @return true if the log should be printed, false otherwise.
     */
    public static boolean shouldLog(double scale) {
        return ThreadLocalRandom.current().nextDouble() < scale;
    }
}
