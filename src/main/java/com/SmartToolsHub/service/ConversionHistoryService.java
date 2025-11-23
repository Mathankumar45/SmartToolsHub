package com.SmartToolsHub.service;

import com.SmartToolsHub.dto.ConversionHistoryItem;
import com.SmartToolsHub.repository.ConversionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversionHistoryService {

    private final ConversionHistoryRepository historyRepository;
    
    public List<ConversionHistoryItem> getUserHistory(UserDetails user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ConversionHistoryItem> historyPage = historyRepository.findByUsername(user.getUsername(), pageable);
        return historyPage.getContent();
    }
    
    public void addToHistory(UserDetails user, String fileName, String targetFormat, String status) {
        ConversionHistoryItem item = new ConversionHistoryItem();
        item.setUsername(user.getUsername());
        item.setOriginalFileName(fileName);
        item.setTargetFormat(targetFormat);
        item.setTimestamp(System.currentTimeMillis());
        item.setStatus(status);
        
        historyRepository.save(item);
        log.info("Added conversion to history for user: {}, file: {}, format: {}, status: {}", 
                user.getUsername(), fileName, targetFormat, status);
    }
    
    public void clearUserHistory(UserDetails user) {
        historyRepository.deleteByUsername(user.getUsername());
        log.info("Cleared conversion history for user: {}", user.getUsername());
    }
}