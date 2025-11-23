package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import org.springframework.web.multipart.MultipartFile;

public interface TxtConverter extends BaseConverter {
    ConversionResult txtToPdf(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult txtToDocx(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult txtToHtml(MultipartFile file, ConversionOptions options) throws Exception;
}