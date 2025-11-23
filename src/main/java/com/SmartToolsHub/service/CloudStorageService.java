package com.SmartToolsHub.service;

import com.SmartToolsHub.dto.CloudFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudStorageService {

    private final GoogleDriveService googleDriveService;
    private final DropboxService dropboxService;
    private final OneDriveService oneDriveService;
    
    public CloudFile importFile(String serviceName, String fileId, UserDetails user) throws Exception {
        switch (serviceName.toLowerCase()) {
            case "google-drive":
                return googleDriveService.getFile(fileId, user);
            case "dropbox":
                return dropboxService.getFile(fileId, user);
            case "onedrive":
                return oneDriveService.getFile(fileId, user);
            default:
                throw new IllegalArgumentException("Unsupported cloud service: " + serviceName);
        }
    }
    
    public List<CloudFile> listFiles(String serviceName, UserDetails user) throws Exception {
        switch (serviceName.toLowerCase()) {
            case "google-drive":
                return googleDriveService.listFiles(user);
            case "dropbox":
                return dropboxService.listFiles(user);
            case "onedrive":
                return oneDriveService.listFiles(user);
            default:
                throw new IllegalArgumentException("Unsupported cloud service: " + serviceName);
        }
    }
    
    public String getAuthUrl(String serviceName) {
        switch (serviceName.toLowerCase()) {
            case "google-drive":
                return googleDriveService.getAuthUrl();
            case "dropbox":
                return dropboxService.getAuthUrl();
            case "onedrive":
                return oneDriveService.getAuthUrl();
            default:
                throw new IllegalArgumentException("Unsupported cloud service: " + serviceName);
        }
    }
    
    public void handleCallback(String serviceName, String code, UserDetails user) throws Exception {
        switch (serviceName.toLowerCase()) {
            case "google-drive":
                googleDriveService.handleCallback(code, user);
                break;
            case "dropbox":
                dropboxService.handleCallback(code, user);
                break;
            case "onedrive":
                oneDriveService.handleCallback(code, user);
                break;
            default:
                throw new IllegalArgumentException("Unsupported cloud service: " + serviceName);
        }
    }
}