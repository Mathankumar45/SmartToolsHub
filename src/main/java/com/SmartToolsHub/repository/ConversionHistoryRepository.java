package com.SmartToolsHub.repository;

import com.SmartToolsHub.dto.ConversionHistoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistoryItem, Long> {
    Page<ConversionHistoryItem> findByUsername(String username, Pageable pageable);
    
    @Modifying
    @Query("DELETE FROM ConversionHistoryItem WHERE username = :username")
    void deleteByUsername(String username);
}