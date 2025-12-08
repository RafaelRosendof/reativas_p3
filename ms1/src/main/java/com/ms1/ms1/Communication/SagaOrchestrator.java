package com.ms1.ms1.Communication;

import java.util.UUID;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class SagaOrchestrator {

    private final StreamBridge streamBridge;

    public void startAnalysis(String ticker){
        String sagaId = UUID.randomUUID().toString();
        System.out.println("MS1: -> Starting saga with ID: " + sagaId + " for ticker: " + ticker + "\n\n\n");
        StockCommandDTO command = new StockCommandDTO(sagaId , ticker);

        streamBridge.send("stock-command-out", command);
        System.out.println("MS1: -> Sent command to process stock data: " + command + "\n\n\n");

    }
}