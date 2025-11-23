package com.SmartToolsHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SmartToolsHub.model.ServiceInquiry;

@Repository
public interface InquiryRepository extends JpaRepository<ServiceInquiry, Long> {
}
