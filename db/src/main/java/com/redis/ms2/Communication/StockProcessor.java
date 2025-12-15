package com.redis.ms2.Communication;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redis.ms2.Service.RedisReactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class StockProcessor {

    private final RedisReactive redisService;

    public StockProcessor(RedisReactive redisService) {

        this.redisService = redisService;
    }

    @Bean
    public Function<Flux<RedisCommandDTO>, Flux<RedisResponseDTO>> processRedis() {
        return flux -> flux
            .doOnNext(cmd -> System.out.println("MS2 (Redis) RECEBEU: " + cmd))
            .flatMap(command -> {
                
                if ("STOP_PROCESS".equals(command.getTicker())) {
                    System.out.println("MS2: PARANDO E PROCESSANDO BATCH...");
                    return redisService.processRedis()
                            .map(msg -> new RedisResponseDTO(
                                    command.getSagaId(),
                                    "BATCH_COMPLETED",
                                    msg,
                                    null
                            ));

                } else {
                    System.out.println("MS2: REGISTRANDO NO REDIS: " + command.getTicker());
                    return redisService.recordStockRequest(command.getTicker())
                            .map(res -> new RedisResponseDTO(
                                    command.getSagaId(),
                                    "SUCCESS",
                                    "Registrado no Redis com sucesso",
                                    null
                            ))
                            .onErrorResume(e -> Mono.just(new RedisResponseDTO(
                                    command.getSagaId(),
                                    "ERROR",
                                    null,
                                    e.getMessage()
                            )));
                }
            })
            .doOnError(err -> System.out.println("ERRO FATAL REDIS PROCESSOR: " + err));
    }

}
