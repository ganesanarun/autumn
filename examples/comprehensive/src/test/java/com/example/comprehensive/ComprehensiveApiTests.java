package com.example.comprehensive;

import org.autumn.AutomationTests;
import org.autumn.reporting.AutumnReportingExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Comprehensive API Tests demonstrating all Autumn framework features
 * 
 * This test class extends AutomationTests which automatically discovers and runs
 * YAML-based tests that demonstrate:
 * - Different HTTP methods (GET, POST, PUT, PATCH, DELETE)
 * - Request/response validation patterns
 * - Environment-specific configurations
 * - Arrange/act/assert patterns
 * - Advanced features like authentication, error handling, and conditional execution
 * 
 * The framework will automatically:
 * 1. Scan for all .yml and .yaml files in src/test/resources/
 * 2. Parse each file as a test case
 * 3. Execute HTTP requests based on the 'act' section
 * 4. Verify responses based on the 'assert' section
 * 5. Generate Allure reports with customization
 * 6. Generate test summaries
 */
@SpringBootTest
@ExtendWith(AutumnReportingExtension.class)
public class ComprehensiveApiTests extends AutomationTests {
    // That's it! No additional code needed.
    //
    // The framework handles:
    // - Test discovery from YAML files
    // - Test execution with arrange/act/assert pattern
    // - HTTP request/response handling
    // - Validation and assertions
    // - Allure report generation and customization
    // - Test summary generation
    //
    // All YAML test files in src/test/resources/ will be automatically
    // discovered and executed as JUnit 5 dynamic tests.
}