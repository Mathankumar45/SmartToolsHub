package com.SmartToolsHub.controller;

import com.SmartToolsHub.model.Feedback;
import com.SmartToolsHub.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FeedbackController {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    
    @Autowired
    private FeedbackService feedbackService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitFeedback(@Valid @RequestBody Feedback feedback) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            logger.info("Received feedback submission from: " + feedback.getEmail());
            Feedback savedFeedback = feedbackService.saveFeedback(feedback);
            
            response.put("success", true);
            response.put("message", "Feedback submitted successfully");
            response.put("data", savedFeedback);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error submitting feedback: " + e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "Failed to submit feedback");
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Feedback API is working");
        return ResponseEntity.ok(response);
    }
}