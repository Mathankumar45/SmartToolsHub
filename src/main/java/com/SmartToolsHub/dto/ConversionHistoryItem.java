package com.SmartToolsHub.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "conversion_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "original_file_name")
    private String originalFileName;
    
    @Column(name = "original_format")
    private String originalFormat;
    
    @Column(name = "target_format")
    private String targetFormat;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "timestamp")
    private Long timestamp;
    
    @Column(name = "status")
    private String status; // COMPLETED, FAILED
}