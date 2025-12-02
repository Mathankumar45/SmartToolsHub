package com.SmartToolsHub.controller;

import com.SmartToolsHub.dto.PaymentRequest;
import com.SmartToolsHub.dto.PaymentResponse;
import com.SmartToolsHub.dto.RazorpayResponse;
import com.SmartToolsHub.dto.StatusResponse;
import com.SmartToolsHub.model.Transaction;
import com.SmartToolsHub.payments.Pay2AllClient;
import com.SmartToolsHub.repository.TransactionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.SmartToolsHub.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

@RestController
public class PaymentController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private Pay2AllClient pay2AllClient;

    @Autowired
    private EmailService emailService; // your existing mail sender

    private final String RAZORPAY_SECRET = "...";

    @PostMapping("/verifyPayment")
    public StatusResponse verifyPayment(@RequestBody RazorpayResponse response) throws Exception {
        Transaction txn = transactionRepository.findByRazorpayOrderId(response.getRazorpay_order_id())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // verify signature (you already have code)
        String data = response.getRazorpay_order_id() + "|" + response.getRazorpay_payment_id();
        String expectedSignature = hmacSHA256(data, RAZORPAY_SECRET);

        if (!expectedSignature.equals(response.getRazorpay_signature())) {
            txn.setStatus("FAILED");
            transactionRepository.save(txn);
            return new StatusResponse("Payment Verification Failed ❌", txn.getTransactionId());
        }

        // Payment verified by Razorpay
        txn.setStatus("PAID");
        txn.setRazorpayPaymentId(response.getRazorpay_payment_id());
        transactionRepository.save(txn);

        // Now call Pay2All to perform recharge (Mobile)
        try {
            // operator & circle: you must determine operator and circle from provider field or via lookup
            String operator = txn.getProvider(); // ensure this matches Pay2All operator names
            String circle = "India"; // replace with actual circle if needed

            Map<String,Object> payResp = pay2AllClient.rechargeMobile(
                    txn.getAccountNumber(), operator, circle, txn.getAmount(), txn.getTransactionId());

            // Example response keys: statusCode / status / vendor_txn_id / message
            String status = (String) payResp.getOrDefault("status","PENDING");
            String vendorId = (String) payResp.get("vendor_txn_id");
            String message = (String) payResp.getOrDefault("message","");

            txn.setProviderResponse(message);
            txn.setRazorpayPaymentId(response.getRazorpay_payment_id());
            txn.setStatus( status.equalsIgnoreCase("SUCCESS") ? "SUCCESS" : status.equalsIgnoreCase("FAILED") ? "FAILED" : "PENDING");
            txn.setRefundId(vendorId); // store vendor id if provided
            transactionRepository.save(txn);

            if ("SUCCESS".equalsIgnoreCase(txn.getStatus())) {
                // send success email
                emailService.sendRechargeCompletedEmail(txn, txn.getUserEmail());
                return new StatusResponse("Payment & Recharge Successful ✅", txn.getTransactionId());
            } else if ("PENDING".equalsIgnoreCase(txn.getStatus())) {
                // schedule a polling / wait for webhook from provider
                return new StatusResponse("Payment verified. Recharge pending — will update when provider responds", txn.getTransactionId());
            } else {
                // FAILED
                // optional: initiate refund via Razorpay APIs (your business decision)
                emailService.sendRechargeCompletedEmail(txn, txn.getUserEmail());
                return new StatusResponse("Payment verified but recharge failed. We will refund or retry.", txn.getTransactionId());
            }

        } catch(Exception ex) {
            // if provider call fails unexpectedly, keep txn as PENDING and alert admin
            txn.setStatus("PENDING");
            txn.setProviderResponse("Provider call error: " + ex.getMessage());
            transactionRepository.save(txn);
            // notify admin via email/log
            emailService.sendAdminAlert("Recharge provider call failed for txn " + txn.getTransactionId(), ex);
            return new StatusResponse("Payment verified. Provider call failed (network). We'll retry.", txn.getTransactionId());
        }
    }


    private String hmacSHA256(String data, String secret) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        mac.init(new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
    
    @PostMapping("/provider/webhook/pay2all")
    public ResponseEntity<String> pay2allWebhook(@RequestBody Map<String,Object> payload, @RequestHeader Map<String,String> headers) {
        // Validate signature if provider sends one
        // Parse payload: vendor_txn_id or client_txn_id
        String clientTxn = (String) payload.get("client_txn_id"); // what we sent earlier
        String status = (String) payload.get("status");

        Transaction txn = transactionRepository.findByTransactionId(clientTxn).orElse(null);
        if(txn == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("txn not found");
        }

        if ("SUCCESS".equalsIgnoreCase(status)) {
            txn.setStatus("SUCCESS");
            txn.setProviderResponse(payload.toString());
            transactionRepository.save(txn);
            emailService.sendRechargeCompletedEmail(txn, txn.getUserEmail());
        } else if ("FAILED".equalsIgnoreCase(status)) {
            txn.setStatus("FAILED");
            transactionRepository.save(txn);
            emailService.sendRechargeCompletedEmail(txn, txn.getUserEmail());
            // optionally refund via Razorpay
        } else {
            txn.setStatus("PENDING");
            transactionRepository.save(txn);
        }

        return ResponseEntity.ok("received");
    }

}
