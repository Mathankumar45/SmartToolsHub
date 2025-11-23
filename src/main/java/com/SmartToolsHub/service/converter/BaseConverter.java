package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import org.springframework.web.multipart.MultipartFile;

public interface BaseConverter {
    ConversionResult convert(MultipartFile file, ConversionOptions options) throws Exception;
}