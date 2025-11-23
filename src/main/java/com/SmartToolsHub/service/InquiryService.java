package com.SmartToolsHub.service;

import com.SmartToolsHub.model.ServiceInquiry;
import com.SmartToolsHub.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryService {
    
    @Autowired
    private InquiryRepository inquiryRepository;
    
    @Autowired
    private EmailService emailService;
    
    public ServiceInquiry saveInquiry(ServiceInquiry inquiry) {
        ServiceInquiry savedInquiry = inquiryRepository.save(inquiry);
        
        // Send email notification to admin
        emailService.sendInquiryNotification(
            savedInquiry.getName(),
            savedInquiry.getEmail(),
            savedInquiry.getPhone(),
            savedInquiry.getServiceType(),
            savedInquiry.getBudget(),
            savedInquiry.getMessage()
        );
        
        return savedInquiry;
    }
}