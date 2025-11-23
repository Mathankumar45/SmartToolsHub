package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import org.springframework.web.multipart.MultipartFile;

public interface PdfConverter extends BaseConverter {
    ConversionResult pdfToDocx(MultipartFile file, ConversionOptions options) throws Exception;
    ConversionResult pdfToImage(MultipartFile file, String imageFormat, ConversionOptions options) throws Exception;
    ConversionResult pdfToTxt(MultipartFile file, ConversionOptions options) throws Exception; // OCR
    ConversionResult pdfToHtml(MultipartFile file, ConversionOptions options) throws Exception;
}