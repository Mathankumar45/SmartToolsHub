package com.SmartToolsHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudFile {
    private String fileId;
    private String fileName;
    private String fileSize;
    private String downloadUrl;
    private String mimeType;
}