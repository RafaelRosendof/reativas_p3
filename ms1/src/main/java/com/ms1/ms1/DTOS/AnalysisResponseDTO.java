package com.ms1.ms1.DTOS;

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

    public String getErrorMessage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getErrorMessage'");
    }

}