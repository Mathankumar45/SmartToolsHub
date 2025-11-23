package com.SmartToolsHub.service.util;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class FileTypeUtil {

    public static String getExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    public static String getMimeType(MultipartFile file) {
        try (InputStream stream = file.getInputStream()) {
            Tika tika = new Tika();

            // Detect MIME type directly from stream
            String mimeType = tika.detect(stream);

            if (mimeType != null) {
                return mimeType;
            }

            return getMimeTypeFromExtension(getExtension(file.getOriginalFilename()));

        } catch (IOException e) {
            return getMimeTypeFromExtension(getExtension(file.getOriginalFilename()));
        }
    }

    private static String getMimeTypeFromExtension(String extension) {
        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "csv":
                return "text/csv";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "txt":
                return "text/plain";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "odt":
                return "application/vnd.oasis.opendocument.text";
            case "ods":
                return "application/vnd.oasis.opendocument.spreadsheet";
            case "odp":
                return "application/vnd.oasis.opendocument.presentation";
            default:
                return "application/octet-stream";
        }
    }
}
