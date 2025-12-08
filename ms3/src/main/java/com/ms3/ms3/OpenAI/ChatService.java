package com.ms3.ms3.OpenAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class ChatService {

    private final prompt prompt;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final String api_key = "";
    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public ChatService(WebClient.Builder webClientBuilder, prompt prompt, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(OPENAI_API_URL).build();
        this.prompt = prompt;
        this.objectMapper = objectMapper;
    }



    public String getPrompt(String collectData) {
        return prompt.gerarPrompt(collectData);
    }


    public Mono<String> getAnalysis(String contextData ,String api_key) {

        String data = getPrompt(contextData);

        System.out.println("\n\n Data received in getAnalysis method: " + data + "\n\n");

        Mono<String> response = sendChatWithPrompt(data, api_key , "gpt-4o-mini" );

        return response;


    }
    

    public Mono<String> sendChatWithPrompt(String userPrompt, String apiKey, String model) {
        OpenAIChatReq chatRequest = new OpenAIChatReq(model, List.of(new OpenAIMessage("user", userPrompt)));

        return webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chatRequest) // O WebClient serializa o objeto para JSON sozinho
                .retrieve()
                .bodyToMono(String.class) // Converte a resposta para String (ou um DTO de resposta)
                .map(jsonResponse -> extractContent(jsonResponse)) // Um método auxiliar para pegar só o texto "content"
                .onErrorResume(e -> {
                    System.err.println("Erro na chamada OpenAI: " + e.getMessage());
                    return Mono.error(new RuntimeException("OpenAI falhou"));
                });
    }

    private String extractContent(String jsonBody) {
        try {
             JsonNode root = objectMapper.readTree(jsonBody);
             return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
             return jsonBody; // Se falhar, retorna o bruto
        }
    }

    
}