package com.SmartToolsHub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usage_log")
@Data
@NoArgsConstructor
public class UsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "tool_name")
    private String toolName;
    
    @Column(name = "usage_date")
    private java.util.Date usageDate;
    
    public UsageLog(User user, String toolName) {
        this.user = user;
        this.toolName = toolName;
        this.usageDate = new java.util.Date();
    }
}