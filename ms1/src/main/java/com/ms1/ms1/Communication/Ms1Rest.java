package com.ms1.ms1.Communication;

import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@EnableWebFlux

public class Ms1Rest {

    private final SagaOrchestrator sagaOrchestrator;
    public Ms1Rest(SagaOrchestrator sagaOrchestrator) {
        this.sagaOrchestrator = sagaOrchestrator;
    }

    @PostMapping("requestStock")
    public String postMethodName(@RequestBody String entity) {
        System.out.println("MS1: -> Received request to analyze stock ticker: " + entity + "\n\n\n");
        sagaOrchestrator.startAnalysis(entity); // ajustar aqui 
        return entity;
    }

    @GetMapping("analysis")
    public String getAIfeedBack() {
        return sagaOrchestrator.getAIfeedback(); // ajustar aqui
    }
    
    
}
