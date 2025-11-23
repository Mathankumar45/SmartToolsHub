package com.SmartToolsHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionJob {
    private String jobId;
    private String fileName;
    private String targetFormat;
    private String status; // QUEUED, PROCESSING, COMPLETED, FAILED
    private String errorMessage;
    private Long createdAt;
    private Long completedAt;
    
    // Additional constructor for convenience
    public ConversionJob(String jobId, String fileName, String targetFormat, String status) {
        this.jobId = jobId;
        this.fileName = fileName;
        this.targetFormat = targetFormat;
        this.status = status;
        this.createdAt = System.currentTimeMillis();
    }
}