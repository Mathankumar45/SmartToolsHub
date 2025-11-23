package com.SmartToolsHub.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResult {
    private String filename;
    private String contentType;
    private byte[] data;
}