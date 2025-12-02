package com.SmartToolsHub.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_ai_results")
@Data
public class SavedAiResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String actionType;
    
    @Column(columnDefinition = "TEXT")
    private String inputText;

    @Column(columnDefinition = "TEXT")
    private String outputText;

    private String ownerEmail; // set in controller if you have auth

    private LocalDateTime createdAt = LocalDateTime.now();
}
