package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PdfConverterImpl implements PdfConverter {

    @Override
    public ConversionResult convert(MultipartFile file, ConversionOptions options) throws Exception {
        return pdfToImage(file, "jpg", options);
    }

    @Override
    public ConversionResult pdfToDocx(MultipartFile file, ConversionOptions options) throws Exception {
        log.info("Converting PDF to DOCX");
        
        try (PDDocument document = PDDocument.load(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            XWPFDocument docxDocument = new XWPFDocument();
            docxDocument.createParagraph().createRun().setText(text);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            docxDocument.write(outputStream);
            
            return new ConversionResult(
                "converted.docx", 
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
                outputStream.toByteArray()
            );
        }
    }

    @Override
    public ConversionResult pdfToImage(MultipartFile file, String imageFormat, ConversionOptions options) throws Exception {
        log.info("Converting PDF to {}", imageFormat);
        
        try (PDDocument document = PDDocument.load(file.getBytes())) {
            PDFRenderer renderer = new PDFRenderer(document);
            
            // FIX: Use correct method signature - just page index parameter
            BufferedImage image = renderer.renderImage(0); // Render first page
            
            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, imageFormat, outputStream);
            
            return new ConversionResult(
                "converted." + imageFormat, 
                "image/" + imageFormat, 
                outputStream.toByteArray()
            );
        }
    }

    @Override
    public ConversionResult pdfToTxt(MultipartFile file, ConversionOptions options) throws Exception {
        log.info("Converting PDF to TXT");
        
        try (PDDocument document = PDDocument.load(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            return new ConversionResult(
                "converted.txt", 
                "text/plain", 
                text.getBytes()
            );
        }
    }

    @Override
    public ConversionResult pdfToHtml(MultipartFile file, ConversionOptions options) throws Exception {
        log.info("Converting PDF to HTML");
        
        try (PDDocument document = PDDocument.load(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            String html = "<html><head><title>Converted PDF</title></head><body><pre>" + text + "</pre></body></html>";
            
            return new ConversionResult(
                "converted.html", 
                "text/html", 
                html.getBytes()
            );
        }
    }
}