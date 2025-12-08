package com.ms3.ms3.Communication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResponseDTO {
    private String sagaId;

    private String status;

    private String finalAnalysis;

}