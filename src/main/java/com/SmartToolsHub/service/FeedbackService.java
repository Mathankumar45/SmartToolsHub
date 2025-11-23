package com.SmartToolsHub.service;

import com.SmartToolsHub.model.Feedback;
import com.SmartToolsHub.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private EmailService emailService;
    
    public Feedback saveFeedback(Feedback feedback) {
        Feedback savedFeedback = feedbackRepository.save(feedback);
        
        // Send email notification to admin
        emailService.sendFeedbackNotification(
            savedFeedback.getName(),
            savedFeedback.getEmail(),
            savedFeedback.getType(),
            savedFeedback.getSubject(),
            savedFeedback.getMessage(),
            savedFeedback.getRating()
        );
        
        return savedFeedback;
    }
}