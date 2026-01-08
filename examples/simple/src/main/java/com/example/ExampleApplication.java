package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Spring Boot application for the example project.
 * 
 * This application demonstrates the Autumn framework's integration with Spring Boot,
 * including auto-configuration of reporting features like test summary generation
 * and Allure report customization.
 */
@SpringBootApplication
@ConfigurationPropertiesScan("org.autumn.reporting")
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}