package com.example.comprehensive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Comprehensive Example Application
 * 
 * This Spring Boot application demonstrates all features of the Autumn testing framework
 * including test summary generation, Allure report customization, and comprehensive
 * YAML-based API testing patterns.
 * 
 * Features demonstrated:
 * - HTTP method testing (GET, POST, PUT, PATCH, DELETE)
 * - Request/response validation patterns
 * - Environment-specific configurations
 * - Arrange/act/assert patterns
 * - Advanced features (authentication, error handling, conditional execution)
 * - Spring Boot auto-configuration integration
 */
@SpringBootApplication
public class ComprehensiveExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComprehensiveExampleApplication.class, args);
    }
}