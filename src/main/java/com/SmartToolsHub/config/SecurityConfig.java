package com.SmartToolsHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
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

                // 🔓 Public API endpoints
                .requestMatchers("/api/yt/**").permitAll()
                .requestMatchers("/api/ai/**").permitAll()
                .requestMatchers("/api/tools/test-email").permitAll()

                // 🔓 Authentication endpoints
                .requestMatchers("/api/auth/register",
                                 "/api/auth/login",
                                 "/api/auth/session").permitAll()

                // 🔓 Public front-end pages (Updated with YouTube downloader)
                .requestMatchers("/download.html",
                                 "/youtube-downloader.html",  // Added this line
                                 "/ai*.html",
                                 "/chatbot*.html",
                                 "/wordcounter.html",
                                 "/datecalculator.html",
                                 "/pdfmerge.html",
                                 "/fileconverter.html").permitAll()  // Added tool pages

                // 🔓 Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/fonts/**", "/webfonts/**").permitAll()  // Added font resources

                // 🔓 Allow access to downloads folder
                .requestMatchers("/downloads/**").permitAll()

                // 🔓 Chrome DevTools and browser resources
                .requestMatchers("/.well-known/**").permitAll()

                // 🔓 Error page
                .requestMatchers("/error").permitAll()

                // 🔓 Root path (for default redirect)
                .requestMatchers("/").permitAll()

                // 🔐 Protected pages and endpoints
                .requestMatchers("/api/convert/**").authenticated()
                .requestMatchers("/resumebuilder.html").authenticated()
                .requestMatchers("/resumetemplates.html").authenticated()
                .requestMatchers("/index.html").authenticated()
                .requestMatchers("/paymenttool.html").authenticated()

                // 🔐 Any other request requires authentication
                .anyRequest().authenticated()
            )

            // Login configuration
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/index.html", true) // always redirect after login
                .failureUrl("/login.html?error=true")
                .permitAll()
            )

            // Logout configuration
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}