package com.SmartToolsHub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    
    public void sendConversionCompleteNotification(UserDetails user, String fileName, String targetFormat) {
        // TODO: Implement notification system
        log.info("Conversion completed for user {}: {} to {}", user.getUsername(), fileName, targetFormat);
    }
    
    public void sendConversionFailedNotification(UserDetails user, String fileName, String targetFormat, String errorMessage) {
        // TODO: Implement notification system
        log.error("Conversion failed for user {}: {} to {}. Error: {}", user.getUsername(), fileName, targetFormat, errorMessage);
    }
}