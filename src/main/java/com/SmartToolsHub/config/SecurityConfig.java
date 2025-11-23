package com.SmartToolsHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/session").permitAll()
                
                // File converter endpoints that need authentication
                .requestMatchers("/api/convert/**").authenticated()
                
                // Static resources and HTML pages
                .requestMatchers("/*.html", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/tools/test-email").permitAll()
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            // Configure form login
            .formLogin(form -> form
                .loginPage("/login.html")           // The URL of our custom login page
                .loginProcessingUrl("/perform_login")  // The URL where the form is submitted
                .defaultSuccessUrl("/index.html", true) // Where to go after successful login
                .failureUrl("/login.html?error=true")   // Where to go after failed login
                .permitAll() // Allow everyone to access the login page
            )
            // Configure logout
            .logout(logout -> logout
                .logoutUrl("/logout")                 // The URL to trigger logout
                .logoutSuccessUrl("/login.html?logout=true") // Where to go after logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        
        return http.build();
    }
}