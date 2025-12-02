package com.SmartToolsHub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SmartToolsHub.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Transaction> findByTransactionId(String transactionId);
}
