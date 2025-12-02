package com.SmartToolsHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SmartToolsHub.model.SavedAiResult;

public interface SavedAiResultRepository extends JpaRepository<SavedAiResult, Long> {
    // Add query methods if needed
}