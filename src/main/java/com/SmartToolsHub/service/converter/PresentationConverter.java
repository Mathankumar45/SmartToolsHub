package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import org.springframework.web.multipart.MultipartFile;

public interface PresentationConverter extends BaseConverter {
    ConversionResult convertPresentation(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception;
    ConversionResult toPdf(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult toImage(MultipartFile file, String imageFormat, ConversionOptions options) throws Exception;
}