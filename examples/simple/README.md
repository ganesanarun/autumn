# Autumn Framework Example Project
```
./gradlew test allureReport
# With Allure report

./gradlew test -Denv=staging
# Specific environment

./gradlew test
# All tests
```bash

## Running Tests

```
│           └── application.properties        # Configuration
│           │   └── create-user.yml
│           │   ├── get-user.yml             # Example test
│           ├── users/
│       └── resources/
│       │               └── ApiTests.java    # Simple test runner
│       │           └── tests/
│       │       └── example/
│       │   └── com/
│       ├── java/
│   └── test/
├── src/
│   └── wrapper/
├── gradle/
├── settings.gradle
├── build.gradle                    # Project dependencies
example-project/
```

## Structure

4. Run tests: `./gradlew test`
3. Update the base URL in `src/test/resources/application.properties`
2. Add your API test YAML files to `src/test/resources/`
1. Clone or download this example

## Quick Start

This is a minimal example project showing how to use the Autumn Framework in your own project.


