package com.SmartToolsHub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${admin.email}")
    private String adminEmail;
    
    public boolean sendFeedbackNotification(String name, String email, 
                                         String type, String subject, String message, Integer rating) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(adminEmail);
            mailMessage.setSubject("New Feedback: " + subject);
            mailMessage.setText(buildFeedbackEmail(name, email, type, subject, message, rating));
            
            mailSender.send(mailMessage);
            logger.info("Feedback notification sent successfully");
            return true;
        } catch (Exception e) {
            logger.error("Failed to send feedback notification: " + e.getMessage(), e);
            // Don't throw exception - just log it
            return false;
        }
    }
    
    public void sendInquiryNotification(String name, String email, 
                                      String phone, String serviceType, String budget, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(adminEmail);
        mailMessage.setSubject("New Service Inquiry: " + serviceType);
        mailMessage.setText(buildInquiryEmail(name, email, phone, serviceType, budget, message));
        
        mailSender.send(mailMessage);
    }
    
    private String buildFeedbackEmail(String name, String email, String type, 
                                   String subject, String message, Integer rating) {
        return String.format(
            "New Feedback Received\n\n" +
            "Name: %s\n" +
            "Email: %s\n" +
            "Type: %s\n" +
            "Rating: %d/5\n" +
            "Subject: %s\n\n" +
            "Message:\n%s\n\n" +
            "Please review and respond as needed.",
            name, email, type, rating, subject, message
        );
    }
    
    private String buildInquiryEmail(String name, String email, String phone, 
                                   String serviceType, String budget, String message) {
        return String.format(
            "New Service Inquiry\n\n" +
            "Name: %s\n" +
            "Email: %s\n" +
            "Phone: %s\n" +
            "Service Type: %s\n" +
            "Budget: %s\n\n" +
            "Message:\n%s\n\n" +
            "Please contact client within 24 hours.",
            name, email, phone, serviceType, budget, message
        );
    }
}