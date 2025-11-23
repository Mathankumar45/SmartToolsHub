package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import org.springframework.web.multipart.MultipartFile;

public interface SpreadsheetConverter extends BaseConverter {
    ConversionResult convertSpreadsheet(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception;
    ConversionResult toCsv(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult toPdf(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult fromCsv(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception;
    ConversionResult csvToPdf(MultipartFile file, ConversionOptions options) throws Exception;
}