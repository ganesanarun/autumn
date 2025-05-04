# An Overview of the Convention-Based API Automation Testing Framework

The Autumn codebase is a convention-based API automation testing framework, designed to streamline and simplify the
testing process. It is built using Java and leverages the Spring Boot framework and Gradle build tool. The key advantage
of this framework is that it allows Quality Assurance (QA) teams to run tests based on a YAML file, eliminating the need
for repetitive test writing.

## Key Components

### 1. TestCase Class

The `TestCase` class is a data model that encapsulates a test case. It consists of two nested static classes, `Act`
and `Assert`. The `Act` class represents the action to be performed, including the URL, HTTP method, and body of the
request. The `Assert` class represents the expected outcome of the action, including the expected status and body of the
response.

### 2. Response Class

The `Response` class is a data model that represents an HTTP response. It contains the HTTP status code and the response
body.

### 3. HttpRequestExecutor Class

The `HttpRequestExecutor` class is responsible for executing HTTP requests. It uses the `RestClient` class from the
Spring framework to send HTTP requests and receive responses. The `execute` method takes a `TestCase` object as an
argument, makes an HTTP request based on the `Act` part of the `TestCase`, and returns a `Response` object.

## Requirements

- Java 21
- Gradle 8.6

## Convention-Based Testing

The framework uses a convention-based approach to testing. Test cases are defined in YAML files, following a specific
format. The `TestCase` class is used to load these YAML files. This approach allows QA teams to define a large number of
test cases quickly and efficiently, without having to write repetitive code.

## Data Processing

The framework uses the Jackson library to process both JSON and YAML data. The `ObjectMapper` class from the Jackson
library is used to convert JSON and YAML data to and from `JsonNode` objects.

## Dynamic Test Execution

The framework leverages the dynamic test capabilities of the JUnit library. Each YAML file is treated as a separate
test, allowing for efficient execution and reporting of test results.

## JSON Assertions with json-unit

The Autumn codebase uses the json-unit library for writing assertions for JSON data in tests. It provides a fluent API
for comparing expected and actual JSON data, and supports various comparison modes, such as ignoring extra fields,
treating null and missing fields as equals, and more. The assertThatJson method from json-unit is used in the Assert
part of the test case to validate the response body.

### Custom Matchers

The application also includes custom matchers like `AnyDateTimeMatcher` and `AnyUUIDMatcher` for specific assertion
needs.

For example, the `AnyDateTimeMatcher` can be used to assert that a given date-time string matches the expected format
without checking the actual value.
This is useful when the actual date-time value is dynamic or not known in advance.

the `AnyUUIDMatcher` can be used to assert that a given UUID string matches the expected format without checking the
actual value.

There are some inbuilt matchers as well, such as `any-string`, `any-number`, etc.

```json
{
  "id": "${json-unit.any-string}",
  "createdAt": "${json-unit.matches:any-date-time}"
}
```

## Build Configuration

The build configuration is defined in the `build.gradle` file. The project uses the Spring Boot Gradle plugin, the
Spring Dependency Management Gradle plugin, and the Test Logger Gradle plugin. The project's dependencies include the
Spring Boot Starter, Jackson Databind, Jackson Dataformat YAML, and Lombok.

## Running the Tests

To run the tests, execute the following command:

```shell
./gradlew test
```

report in console will look like this:

```shell
> Task :test

org.andino.autumn.AutomationTests

  runAllTestCasesInResources()

    Test successfully-add-macbook.yml PASSED (1.1s)
    Test successfully-get-macbook-by-id.yml PASSED

SUCCESS: Executed 2 tests in 1.7s
```

To run test per environment

```shell
./gradlew test -Penv=<env>
```

The framework will look for test cases in the `src/test/resources/<env>` directory, where `<env>` is the
environment name.

For example, to run tests in the `staging` environment, you would execute:

```shell
./gradlew test -Penv=staging
```

to run tests in the `production` environment, you would execute:

```shell
./gradlew test -Penv=production
```

html report will be generated in `build/reports/tests/test/index.html`

## Future Enhancements

In the future, the Autumn framework will be extended to support calling multiple APIs either sequentially or in
parallel. This will involve extending the TestCase class to support multiple Act and Assert instances, modifying the
HttpRequestExecutor class to execute multiple HTTP requests, and updating the test execution and assertion logic to
handle multiple test cases and responses. This enhancement will allow QA teams to define complex test scenarios
involving multiple API calls and assert the results in a flexible and efficient manner

## Conclusion

The Autumn codebase is a powerful tool for API automation testing.
Its convention-based approach, combined with the dynamic test capabilities of JUnit,
allows QA teams to define and run a large number of tests quickly and efficiently,
without having to write repetitive code. It provides a good example of how to use the Spring Boot framework,
the Jackson library, and the JUnit library in a Java project effectively. The planned enhancements will further increase
the flexibility and power of the Autumn framework, making it an even more valuable tool for API automation testing.