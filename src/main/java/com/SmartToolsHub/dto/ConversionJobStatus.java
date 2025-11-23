package com.SmartToolsHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionJobStatus {
    private String jobId;
    private String status;
    private Integer progressPercentage;
    private String currentStep;
    private String downloadUrl; // Available when status is COMPLETED
    private String errorMessage; // Available when status is FAILED
}