package com.ms1.ms1.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisResponseDTO{
    private String sagaId;

    private String status;

    private String collectedData;

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }
}