package com.ms3.ms3.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.ms3.ms3.OpenAI.ChatService;

@Service
public class AiService {

    private final ChatService chatService;

    @Value("${openai.api.key:NOT_DEFINED}")
    private String openAiApiKey;

    public AiService(ChatService chatService) {
        this.chatService = chatService;
    }

    public Mono<String> ReturnAiAnalysis(String textToAi) {
        return chatService.getAnalysis(textToAi, openAiApiKey);
    }
}