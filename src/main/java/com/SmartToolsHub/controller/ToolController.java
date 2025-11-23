package com.SmartToolsHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/tools")
public class ToolController {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@GetMapping("/test-email")
	public String testEmail() {
	    try {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo("mathanpraveen45@outlook.com");
	        message.setSubject("SMTP Test");
	        message.setText("Your SMTP setup works!");

	        mailSender.send(message);
	        return "Email Sent!";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: " + e.getMessage();
	    }
	}


    @GetMapping
    public List<Map<String, String>> getTools() {

        List<Map<String, String>> tools = new ArrayList<>();

        tools.add(Map.of("name", "Word Counter", "link", "wordcounter.html", "category", "Utilities"));
        tools.add(Map.of("name", "PDF Merger", "link", "pdfmerge.html", "category", "Converters"));
        tools.add(Map.of("name", "Date Calculator", "link", "datecalculator.html", "category", "Calculators"));
        tools.add(Map.of("name", "Resume Builder", "link", "resumebuilder.html", "category", "Generators"));

        return tools;
    }
}
