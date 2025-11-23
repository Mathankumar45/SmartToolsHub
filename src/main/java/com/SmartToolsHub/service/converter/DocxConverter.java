package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import org.springframework.web.multipart.MultipartFile;

public interface DocxConverter extends BaseConverter {
    ConversionResult docxToPdf(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult docxToTxt(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult docxToHtml(MultipartFile file, ConversionOptions options) throws Exception;
}