package com.ms1.ms1.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisCommandDTO{
    private String sagaId;

    private String context;
    
    private String promptInstructions;
}