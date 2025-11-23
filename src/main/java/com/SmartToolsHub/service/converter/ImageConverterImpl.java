package com.SmartToolsHub.service.converter;

import com.SmartToolsHub.dto.ConversionOptions;
import com.SmartToolsHub.service.model.ConversionResult;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
public class ImageConverterImpl implements ImageConverter {

    @Override
    public ConversionResult convert(MultipartFile file, ConversionOptions options) throws Exception {
        return imageToPdf(file, options);
    }

    @Override
    public ConversionResult imageToPdf(MultipartFile file, ConversionOptions options) throws Exception {
        log.info("Converting image to PDF");
        
        BufferedImage image = ImageIO.read(file.getInputStream());
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();
                float imgWidth = image.getWidth();
                float imgHeight = image.getHeight();
                
                float scale = Math.min(pageWidth / imgWidth, pageHeight / imgHeight) * 0.9f;
                float x = (pageWidth - imgWidth * scale) / 2;
                float y = (pageHeight - imgHeight * scale) / 2;
                
                // FIX: Convert BufferedImage to PDImageXObject first
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(
                    document, 
                    imageToByteArray(image, "jpg"), 
                    "jpg"
                );
                
                // FIX: Use correct drawImage method signature
                contentStream.drawImage(pdImage, x, y, imgWidth * scale, imgHeight * scale);
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            
            return new ConversionResult(
                "converted.pdf", 
                "application/pdf", 
                outputStream.toByteArray()
            );
        }
    }

    @Override
    public ConversionResult imageToDocx(MultipartFile file, ConversionOptions options) throws Exception {
        log.info("Converting image to DOCX");
        
        BufferedImage image = ImageIO.read(file.getInputStream());
        
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream imageStream = new ByteArrayOutputStream()) {
            
            ImageIO.write(image, "jpeg", imageStream);
            
            int pictureType = determinePictureType(file.getOriginalFilename());
            document.createParagraph().createRun().addPicture(
                new ByteArrayInputStream(imageStream.toByteArray()), 
                pictureType, 
                file.getOriginalFilename(), 
                image.getWidth(), 
                image.getHeight()
            );
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            
            return new ConversionResult(
                "converted.docx", 
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
                outputStream.toByteArray()
            );
        }
    }

    @Override
    public ConversionResult imageToTxt(MultipartFile file, ConversionOptions options) throws Exception {
        log.info("Converting image to TXT using OCR");
        
        BufferedImage image = ImageIO.read(file.getInputStream());
        
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage(options.getOcrLanguage());
        
        String text = tesseract.doOCR(image);
        
        return new ConversionResult(
            "converted.txt", 
            "text/plain", 
            text.getBytes()
        );
    }

    @Override
    public ConversionResult convertImage(MultipartFile file, String targetFormat, ConversionOptions options) throws Exception {
        log.info("Converting image to {}", targetFormat);
        
        BufferedImage image = ImageIO.read(file.getInputStream());
        
        int width = options.getWidth() != null ? options.getWidth() : image.getWidth();
        int height = options.getHeight() != null ? options.getHeight() : image.getHeight();
        
        if (options.getMaintainAspectRatio()) {
            double aspectRatio = (double) image.getWidth() / image.getHeight();
            if (width / height > aspectRatio) {
                width = (int) (height * aspectRatio);
            } else {
                height = (int) (width / aspectRatio);
            }
        }
        
        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());
        resizedImage.getGraphics().drawImage(image, 0, 0, width, height, null);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, targetFormat, outputStream);
        
        return new ConversionResult(
            "converted." + targetFormat, 
            "image/" + targetFormat, 
            outputStream.toByteArray()
        );
    }
    
    private int determinePictureType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "png":
                return XWPFDocument.PICTURE_TYPE_PNG;
            case "jpeg":
            case "jpg":
                return XWPFDocument.PICTURE_TYPE_JPEG;
            case "gif":
                return XWPFDocument.PICTURE_TYPE_GIF;
            case "bmp":
                return XWPFDocument.PICTURE_TYPE_BMP;
            case "emf":
                return XWPFDocument.PICTURE_TYPE_EMF;
            case "wmf":
                return XWPFDocument.PICTURE_TYPE_WMF;
            default:
                return XWPFDocument.PICTURE_TYPE_JPEG;
        }
    }
    
    // Helper method to convert BufferedImage to byte array
    private byte[] imageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }
}