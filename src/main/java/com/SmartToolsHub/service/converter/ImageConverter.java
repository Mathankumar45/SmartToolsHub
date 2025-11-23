package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImageConverter extends BaseConverter {
    ConversionResult imageToPdf(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult imageToDocx(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult imageToTxt(MultipartFile file, ConversionOptions options) throws Exception; // OCR
    ConversionResult convertImage(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception;
}