package com.SmartToolsHub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TemporaryFileStorage {
    
    private final Map<String, byte[]> fileStorage = new ConcurrentHashMap<>();
    
    public void store(String jobId, byte[] data) {
        fileStorage.put(jobId, data);
        log.info("Stored temporary file for job: {}", jobId);
    }
    
    public byte[] retrieve(String jobId) {
        return fileStorage.get(jobId);
    }
    
    public void remove(String jobId) {
        fileStorage.remove(jobId);
        log.info("Removed temporary file for job: {}", jobId);
    }
}