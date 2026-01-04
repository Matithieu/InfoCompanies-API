package com.example.spring.app.llm;

public class LLMUtils {
    /**
     * Parse conversation title to limit to max 6 words.
     * If more than 6 words, append "..." at the end.
     */
    public static String parseConversationTitle(String conversationTitle) {
        String parsedTitle = conversationTitle;
        if (conversationTitle != null) {
            String[] words = conversationTitle.split("\\s+");
            if (words.length > 6) {
                parsedTitle = String.join(" ", java.util.Arrays.copyOfRange(words, 0, 6)) + "...";
            }
        }
        return parsedTitle;
    }
}
