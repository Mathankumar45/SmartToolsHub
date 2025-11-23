package com.SmartToolsHub.controller;

import com.SmartToolsHub.model.ServiceInquiry;
import com.SmartToolsHub.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inquiry")
@CrossOrigin(origins = "*")
public class InquiryController {
    
    @Autowired
    private InquiryService inquiryService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitInquiry(@Valid @RequestBody ServiceInquiry inquiry) {
        try {
            ServiceInquiry savedInquiry = inquiryService.saveInquiry(inquiry);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Inquiry submitted successfully");
            response.put("data", savedInquiry);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to submit inquiry: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}