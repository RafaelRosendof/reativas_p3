package com.ms3.ms3.Communication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;
import com.ms3.ms3.Service.AiService;

import reactor.core.publisher.Mono;

@Configuration
public class AiProcessor {

    private final AiService aiService;

    public AiProcessor(AiService aiService) {
        this.aiService = aiService;
    }

    @Bean
    public Function<AnalysisCommandDTO, Mono<AnalysisResponseDTO>> processAnalysis(){
        return command -> {

            System.out.println("MS3: -> Received command to process AI analysis: " + command + "\n\n\n");
            return aiService.ReturnAiAnalysis(command.getContext())
                .map(analise -> new AnalysisResponseDTO(
                        command.getSagaId(), 
                        "SUCCESS", 
                        analise
                ))
                .onErrorResume(e -> {
                    System.err.println("Erro no processamento reativo: " + e.getMessage());
                    return Mono.just(new AnalysisResponseDTO(
                        command.getSagaId(), 
                        "ERROR", 
                        "Falha: " + e.getMessage()
                    ));
                });
        };
    }

}