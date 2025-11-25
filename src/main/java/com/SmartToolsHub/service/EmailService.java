package com.SmartToolsHub.service;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${admin.email}")
    private String adminEmail;

    private static final String FROM_EMAIL = "mathanpravee368@gmail.com"; // Your verified sender

    // ---------------- FEEDBACK NOTIFICATION ----------------

    public boolean sendFeedbackNotification(String name, String email, 
                                            String type, String subject, 
                                            String message, Integer rating) {
        try {

            String body = buildFeedbackEmail(name, email, type, subject, message, rating);

            boolean sent = sendEmail(adminEmail, "New Feedback: " + subject, body);

            if (sent) {
                logger.info("Feedback email sent successfully");
            } else {
                logger.error("Feedback email sending failed");
            }

            return sent;

        } catch (Exception e) {
            logger.error("Failed to send feedback notification: " + e.getMessage(), e);
            return false;
        }
    }

    // ---------------- INQUIRY NOTIFICATION ----------------

    public void sendInquiryNotification(String name, String email,
                                        String phone, String serviceType,
                                        String budget, String message) {

        String body = buildInquiryEmail(name, email, phone, serviceType, budget, message);

        boolean sent = sendEmail(adminEmail, "New Service Inquiry: " + serviceType, body);

        if (sent) {
            logger.info("Inquiry email sent successfully");
        } else {
            logger.error("Inquiry email failed");
        }
    }

    // ---------------- SENDGRID EMAIL SENDER ----------------

    private boolean sendEmail(String toEmail, String subject, String contentText) {
        try {
            Email from = new Email(FROM_EMAIL);
            Email to = new Email(toEmail);
            Content content = new Content("text/plain", contentText);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sg.api(request);
            return true;

        } catch (Exception e) {
            logger.error("SendGrid Email Error: " + e.getMessage(), e);
            return false;
        }
    }

    // ---------------- EMAIL BODY BUILDERS ----------------

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
