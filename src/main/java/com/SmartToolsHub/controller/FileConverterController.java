package com.SmartToolsHub.controller;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.FileConverterService;
import com.SmartToolsHub.service.model.ConversionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/convert")
@Slf4j
public class FileConverterController {

    private final FileConverterService service;

    @PostMapping
    public ResponseEntity<byte[]> convert(
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") String format,
            @RequestParam(value = "options", required = false) String optionsJson
    ) {
        try {
            // Only parse options if provided
            ConversionOptions options = optionsJson != null ? 
                    ConversionOptions.fromJson(optionsJson) : 
                    service.getDefaultOptions(
                        com.SmartToolsHub.service.util.FileTypeUtil.getExtension(file.getOriginalFilename()), 
                        format
                    );
            
            ConversionResult result = service.convert(file, format, options);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getFilename())
                    .contentType(MediaType.parseMediaType(result.getContentType()))
                    .body(result.getData());

        } catch (Exception e) {
            log.error("Conversion failed for file: {}", file.getOriginalFilename(), e);
            
            // Return a more informative error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Conversion failed: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/formats")
    public ResponseEntity<Map<String, java.util.List<String>>> getSupportedFormats() {
        // FIX: Return Map<String, List<String>> instead of Map<String, String>
        Map<String, java.util.List<String>> formats = service.getSupportedFormats();
        return ResponseEntity.ok(formats);
    }

    @PostMapping("/options")
    public ResponseEntity<ConversionOptions> getConversionOptions(
            @RequestParam("inputFormat") String inputFormat,
            @RequestParam("outputFormat") String outputFormat
    ) {
        try {
            ConversionOptions options = service.getDefaultOptions(inputFormat, outputFormat);
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            log.error("Failed to get conversion options for {} to {}", inputFormat, outputFormat, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}