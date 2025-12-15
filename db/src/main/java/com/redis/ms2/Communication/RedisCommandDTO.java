package com.redis.ms2.Communication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisCommandDTO {
    private String sagaId;
    private String ticker;
}