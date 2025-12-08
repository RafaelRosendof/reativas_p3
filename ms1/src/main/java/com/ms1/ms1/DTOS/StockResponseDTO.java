package com.ms1.ms1.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDTO{
    private String sagaId;

    private String collectedData;

    private String errorMessage;
}