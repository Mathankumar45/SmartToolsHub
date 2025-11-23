package com.SmartToolsHub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionOptions {
    // PDF options
    private String pdfQuality = "medium"; // low, medium, high
    private Boolean compressPdf = false;
    private String pdfPassword;
    
    // Image options
    private Integer imageQuality = 80; // 10-100
    private Integer width;
    private Integer height;
    private Boolean maintainAspectRatio = true;
    
    // Document options
    private Boolean preserveFormatting = true;
    private String pageSize = "A4"; // A4, Letter, Legal
    
    // OCR options
    private String ocrLanguage = "eng"; // ISO 639-2 language codes
    
    // Helper methods for JSON serialization
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static ConversionOptions fromJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return new ConversionOptions();
            }
            
            // Use Jackson ObjectMapper for proper JSON parsing
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return objectMapper.readValue(json, ConversionOptions.class);
        } catch (Exception e) {
            // Return default options if parsing fails
            return new ConversionOptions();
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public String toJson() {
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}