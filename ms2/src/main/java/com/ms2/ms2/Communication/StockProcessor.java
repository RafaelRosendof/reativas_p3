package com.ms2.ms2.Communication;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ms2.ms2.Service.StockService;

import reactor.core.publisher.Mono;

@Configuration
public class StockProcessor {

    private final StockService stockService;

    public StockProcessor(StockService stockService) {
        this.stockService = stockService;
    }

    @Bean 
    public Function<StockCommandDTO , Mono<StockResponseDTO>> processStockData(){
        return command -> {
            System.out.println("MS2: -> Received command to process stock data: " + command + "\n\n\n");
            return stockService.collectData(command.getTicker())
            .map(data -> new StockResponseDTO(command.getId(), "SUCCESS", data))
            .onErrorResume(e -> Mono.just(new StockResponseDTO(command.getId(), "ERROR", null)));
        };
    }
}