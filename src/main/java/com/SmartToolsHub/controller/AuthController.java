package com.SmartToolsHub.controller;

import com.SmartToolsHub.model.User;
import com.SmartToolsHub.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        try {
            log.info("Registering user: {}", user.getUsername());
            
            User registeredUser = authService.register(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", registeredUser.getUsername());
            
            log.info("User registered successfully: {}", registeredUser.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Registration failed for user: {}", user.getUsername(), e);
            
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> session(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        if (userDetails != null) {
            response.put("loggedIn", true);
            response.put("username", userDetails.getUsername());
            log.debug("User session active: {}", userDetails.getUsername());
        } else {
            response.put("loggedIn", false);
            log.debug("No active user session");
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            log.info("Login attempt for user: {}", username);
            
            // This is just for debugging - actual authentication is handled by Spring Security
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login endpoint hit");
            response.put("username", username);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login error", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}