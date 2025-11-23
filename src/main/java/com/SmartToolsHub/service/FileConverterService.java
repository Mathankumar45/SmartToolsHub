package com.SmartToolsHub.service;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.converter.*;
import com.SmartToolsHub.service.model.ConversionResult;
import com.SmartToolsHub.service.util.FileTypeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileConverterService {

    private final PdfConverter pdfConverter;
    private final DocxConverter docxConverter;
    private final TxtConverter txtConverter;
    private final ImageConverter imageConverter;
    private final SpreadsheetConverter spreadsheetConverter;
    private final PresentationConverter presentationConverter;

    public ConversionResult convert(MultipartFile file, String targetFormat) throws Exception {
        // Create default options if not provided
        ConversionOptions options = getDefaultOptions(
            FileTypeUtil.getExtension(file.getOriginalFilename()), 
            targetFormat
        );
        
        return convert(file, targetFormat, options);
    }

    public ConversionResult convert(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        String ext = FileTypeUtil.getExtension(file.getOriginalFilename());
        
        // Validate file
        validateFile(file, ext);
        
        return performConversion(file, ext, targetFormat, options);
    }

    // FIX: Return Map<String, List<String>> instead of Map<String, String>
    public Map<String, java.util.List<String>> getSupportedFormats() {
        Map<String, java.util.List<String>> formats = new HashMap<>();
        
        formats.put("pdf", Arrays.asList("docx", "jpg", "png", "txt", "html"));
        formats.put("docx", Arrays.asList("pdf", "txt", "html"));
        formats.put("txt", Arrays.asList("pdf", "docx", "html"));
        formats.put("jpg", Arrays.asList("pdf", "docx", "txt", "png"));
        formats.put("jpeg", Arrays.asList("pdf", "docx", "txt", "png"));
        formats.put("png", Arrays.asList("pdf", "docx", "txt", "jpg"));
        formats.put("gif", Arrays.asList("pdf", "jpg", "png"));
        formats.put("xls", Arrays.asList("xlsx", "csv", "pdf"));
        formats.put("xlsx", Arrays.asList("xls", "csv", "pdf"));
        formats.put("csv", Arrays.asList("xls", "xlsx", "pdf"));
        formats.put("ppt", Arrays.asList("pptx", "pdf", "jpg"));
        formats.put("pptx", Arrays.asList("ppt", "pdf", "jpg"));
        
        return formats;
    }

    public ConversionOptions getDefaultOptions(String inputFormat, String outputFormat) {
        ConversionOptions options = new ConversionOptions();
        
        // Set default options based on conversion type
        if ("pdf".equals(outputFormat)) {
            options.setPdfQuality("medium");
            options.setCompressPdf(true);
        } else if (Arrays.asList("jpg", "jpeg", "png").contains(outputFormat)) {
            options.setImageQuality(80);
            options.setMaintainAspectRatio(true);
        } else if ("docx".equals(outputFormat)) {
            options.setPreserveFormatting(true);
            options.setPageSize("A4");
        }
        
        return options;
    }

    private ConversionResult performConversion(MultipartFile file, String inputFormat, 
                                             String targetFormat, ConversionOptions options) throws Exception {
        switch (inputFormat) {
            case "pdf":
                return convertPdf(file, targetFormat, options);
            case "docx":
                return convertDocx(file, targetFormat, options);
            case "txt":
                return convertTxt(file, targetFormat, options);
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return convertImage(file, targetFormat, options);
            case "xls":
            case "xlsx":
                return convertSpreadsheet(file, targetFormat, options);
            case "csv":
                return convertCsv(file, targetFormat, options);
            case "ppt":
            case "pptx":
                return convertPresentation(file, targetFormat, options);
            default:
                throw new RuntimeException("Unsupported input format: " + inputFormat);
        }
    }

    private ConversionResult convertPdf(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        switch (targetFormat) {
            case "docx":
                return pdfConverter.pdfToDocx(file, options);
            case "jpg":
            case "png":
                return pdfConverter.pdfToImage(file, targetFormat, options);
            case "txt":
                return pdfConverter.pdfToTxt(file, options);
            case "html":
                return pdfConverter.pdfToHtml(file, options);
            default:
                throw new RuntimeException("Unsupported conversion from PDF to " + targetFormat);
        }
    }

    private ConversionResult convertDocx(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        switch (targetFormat) {
            case "pdf":
                return docxConverter.docxToPdf(file, options);
            case "txt":
                return docxConverter.docxToTxt(file, options);
            case "html":
                return docxConverter.docxToHtml(file, options);
            default:
                throw new RuntimeException("Unsupported conversion from DOCX to " + targetFormat);
        }
    }

    private ConversionResult convertTxt(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        switch (targetFormat) {
            case "pdf":
                return txtConverter.txtToPdf(file, options);
            case "docx":
                return txtConverter.txtToDocx(file, options);
            case "html":
                return txtConverter.txtToHtml(file, options);
            default:
                throw new RuntimeException("Unsupported conversion from TXT to " + targetFormat);
        }
    }

    private ConversionResult convertImage(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        switch (targetFormat) {
            case "pdf":
                return imageConverter.imageToPdf(file, options);
            case "docx":
                return imageConverter.imageToDocx(file, options);
            case "txt":
                return imageConverter.imageToTxt(file, options);
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return imageConverter.convertImage(file, targetFormat, options);
            default:
                throw new RuntimeException("Unsupported conversion from image to " + targetFormat);
        }
    }

    private ConversionResult convertSpreadsheet(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        switch (targetFormat) {
            case "xls":
            case "xlsx":
                return spreadsheetConverter.convertSpreadsheet(file, targetFormat, options);
            case "csv":
                return spreadsheetConverter.toCsv(file, options);
            case "pdf":
                return spreadsheetConverter.toPdf(file, options);
            default:
                throw new RuntimeException("Unsupported conversion from spreadsheet to " + targetFormat);
        }
    }

    private ConversionResult convertCsv(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        switch (targetFormat) {
            case "xls":
            case "xlsx":
                return spreadsheetConverter.fromCsv(file, targetFormat, options);
            case "pdf":
                return spreadsheetConverter.csvToPdf(file, options);
            default:
                throw new RuntimeException("Unsupported conversion from CSV to " + targetFormat);
        }
    }

    private ConversionResult convertPresentation(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        switch (targetFormat) {
            case "ppt":
            case "pptx":
                return presentationConverter.convertPresentation(file, targetFormat, options);
            case "pdf":
                return presentationConverter.toPdf(file, options);
            case "jpg":
                return presentationConverter.toImage(file, "jpg", options);
            default:
                throw new RuntimeException("Unsupported conversion from presentation to " + targetFormat);
        }
    }

    private void validateFile(MultipartFile file, String ext) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        // Check file size (e.g., 50MB limit)
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds limit (50MB)");
        }
    }
}