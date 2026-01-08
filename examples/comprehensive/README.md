# Comprehensive Autumn Framework Example

This example project demonstrates all features and capabilities of the Autumn testing framework through comprehensive YAML-based API tests.

## Features Demonstrated

### HTTP Methods
- **GET Requests**: Basic retrieval and query parameter usage
- **POST Requests**: Resource creation with request body validation
- **PUT Requests**: Complete resource updates
- **PATCH Requests**: Partial resource updates
- **DELETE Requests**: Resource deletion

### Request/Response Validation
- **Request Validation**: Headers, query parameters, and request body validation
- **Flexible Response Validation**: Using json-unit matchers for dynamic content
- **Strict Response Validation**: Exact matching with strict validation rules
- **Regex Validation**: Pattern matching for structured data (emails, phone numbers, etc.)

### Arrange-Act-Assert Patterns
- **Simple Pattern**: Basic data setup and retrieval
- **Complex Pattern**: Multi-step setup with data dependencies
- **Data Generation**: Dynamic test data generation using framework functions

### Environment-Specific Configuration
- **Staging Environment**: Environment-specific URLs, headers, and scheduling
- **Production Environment**: Production-safe testing with security considerations
- **Local Development**: Flexible validation for development environments

### Advanced Features
- **Error Handling**: Testing error scenarios and error response validation
- **Authentication**: Bearer token and Basic authentication examples
- **Timeout and Retry**: Resilient testing with timeout and retry configuration
- **Conditional Execution**: Environment-based conditional test execution

## Project Structure

```
examples/comprehensive/
├── src/
│   ├── main/
│   │   ├── java/com/example/comprehensive/
│   │   │   └── ComprehensiveExampleApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-staging.properties
│   │       └── application-production.properties
│   └── test/
│       ├── java/com/example/comprehensive/
│       │   └── ComprehensiveApiTests.java
│       └── resources/
│           ├── http-methods/
│           │   ├── get-user.yaml
│           │   ├── get-users-with-query.yaml
│           │   ├── post-create-user.yaml
│           │   ├── put-update-user.yaml
│           │   ├── patch-partial-update.yaml
│           │   └── delete-user.yaml
│           ├── validation/
│           │   ├── request-validation.yaml
│           │   ├── response-validation-flexible.yaml
│           │   ├── response-validation-strict.yaml
│           │   └── regex-validation.yaml
│           ├── arrange-act-assert/
│           │   ├── simple-arrange-act-assert.yaml
│           │   ├── complex-arrange-act-assert.yaml
│           │   └── data-generation.yaml
│           ├── environment/
│           │   ├── staging-environment.yaml
│           │   ├── production-environment.yaml
│           │   └── local-development.yaml
│           └── advanced/
│               ├── error-handling.yaml
│               ├── authentication.yaml
│               ├── basic-auth.yaml
│               ├── timeout-retry.yaml
│               └── conditional-execution.yaml
├── build.gradle
└── README.md
```

## Setup and Installation

### Prerequisites
- Java 21 or higher
- Gradle 8.0 or higher

### Installation
1. Clone the repository
2. Navigate to the comprehensive example directory:
   ```bash
   cd examples/comprehensive
   ```
3. Build the project:
   ```bash
   ./gradlew build
   ```

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Tests with Different Environments
```bash
# Run with staging configuration
./gradlew test -Dspring.profiles.active=staging

# Run with production configuration
./gradlew test -Dspring.profiles.active=production

# Run with custom environment parameter
./gradlew test -Denv=staging
```

### Run Specific Test Categories
```bash
# Run only HTTP method tests
./gradlew test --tests "*testGet*" --tests "*testPost*" --tests "*testPut*" --tests "*testPatch*" --tests "*testDelete*"

# Run only validation tests
./gradlew test --tests "*Validation*"

# Run only arrange-act-assert tests
./gradlew test --tests "*ArrangeActAssert*"

# Run only environment tests
./gradlew test --tests "*Environment*"

# Run only advanced feature tests
./gradlew test --tests "*testError*" --tests "*testAuth*" --tests "*testTimeout*" --tests "*testConditional*"
```

## Configuration Examples

### Test Summary Generation
The framework automatically generates test summaries when enabled:
```properties
# Enable test summary generation
autumn.reporting.test-summary.enabled=true
autumn.reporting.test-summary.output-path=build/test-results/summary.json
```

### Allure Report Customization
Customize Allure reports with your branding:
```properties
# Enable Allure report customization
autumn.reporting.allure-customization.enabled=true
autumn.reporting.allure-customization.title=Comprehensive Example Test Results
autumn.reporting.allure-customization.brand-name=Autumn Framework
```

### Environment-Specific Configuration
Configure different environments:
```properties
# API endpoints for different environments
api.base-url.staging=https://api-staging.example.com
api.base-url.production=https://api.example.com
api.base-url.local=http://localhost:8080

# Environment-specific timeouts
api.timeout=30000
api.retry-attempts=3
```

## Report Generation and Viewing

### Generate Test Summary
Test summaries are automatically generated after test execution:
```bash
./gradlew test
# View summary at: build/test-results/summary.json
```

### Generate and View Allure Reports
```bash
# Run tests and generate Allure report
./gradlew test allureReport

# Serve Allure report locally
./gradlew allureServe
```

The Allure report will be available at `http://localhost:8080` with custom branding applied.

### View Test Results
- **Console Output**: Detailed test execution logs with the test-logger plugin
- **JUnit Reports**: Standard JUnit XML reports in `build/test-results/test/`
- **Test Summary**: JSON summary in `build/test-results/summary.json`
- **Allure Reports**: Rich HTML reports in `build/reports/allure-report/`

## Troubleshooting

### Common Issues

1. **Tests failing due to network issues**
   - Check internet connectivity
   - Verify API endpoints are accessible
   - Consider using local mock servers for offline testing

2. **Configuration not loading**
   - Verify property file names and locations
   - Check Spring profile activation
   - Ensure property syntax is correct

3. **Allure reports not generating**
   - Verify Allure plugin configuration
   - Check that tests are actually running
   - Ensure output directory permissions

4. **Environment-specific tests not running**
   - Verify Spring profiles are activated correctly
   - Check conditional execution logic
   - Ensure environment properties are set

### Debug Mode
Enable debug logging for troubleshooting:
```bash
./gradlew test -Dlogging.level.org.autumn=DEBUG -Dlogging.level.com.example=DEBUG
```

## Contributing

When adding new test examples:
1. Follow the existing directory structure
2. Include comprehensive documentation in YAML files
3. Add corresponding test methods in `ComprehensiveApiTests.java`
4. Update this README with new features
5. Ensure tests are environment-agnostic where possible

## License

This example project is part of the Autumn testing framework and follows the same license terms.