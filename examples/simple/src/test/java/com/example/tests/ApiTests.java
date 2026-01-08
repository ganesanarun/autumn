package com.example.tests;

import org.autumn.AutomationTests;
import org.autumn.reporting.AutumnReportingExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Main test runner for API tests.
 *
 * This class extends AutomationTests which will automatically discover
 * and execute all YAML test files in src/test/resources/.
 *
 * The framework will:
 * 1. Scan for all .yml and .yaml files
 * 2. Parse each file as a test case
 * 3. Execute HTTP requests based on the 'act' section
 * 4. Verify responses based on the 'assert' section
 * 5. Generate Allure reports
 * 6. Generate test summaries (via Gradle task)
 * 7. Customize Allure reports (via AutumnReportingExtension)
 */
@SpringBootTest
@ExtendWith(AutumnReportingExtension.class)
public class ApiTests extends AutomationTests {
    // That's it! No additional code needed.
    //
    // The framework handles:
    // - Test discovery
    // - Test execution
    // - Reporting
    // - Test summary generation (via Gradle task)
    // - Allure report customization (via Spring Boot auto-configuration)
    //
    // Just add your YAML test files to src/test/resources/
}

