package org.andino.autumn;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.andino.autumn.matchers.AnyUUIDMatcher;
import org.andino.autumn.matchers.AnyZonedDateTimeMatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_ARRAY_ITEMS;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


public class AutomationTests {

    private final HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor(RestClient.create(), new ObjectMapper());
    private final YamlReader yamlReader = new YamlReader();
    private static final Logger LOGGER = LoggerFactory.getLogger(AutomationTests.class);
    private static String resourcePattern = "classpath*:*.y*ml";

    @BeforeAll
    public static void setup() {
        String env = System.getProperty("env", "");
        LOGGER.info("Using environment: {}", env);
        if (!env.isEmpty()) {
            resourcePattern = "classpath*:" + env + "/*.y*ml";
        }
        LOGGER.info("Loading resource pattern: {}", resourcePattern);
    }

    @TestFactory
    public Collection<DynamicTest> tests() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(resourcePattern);

        return Stream.of(resources).parallel().map(resource -> dynamicTest(resource.getFilename(), () -> {
            var fileName = getFileName(resource.getFilename());
            TestCase testCase = yamlReader.readYamlFile(fileName);

            Response response = httpRequestExecutor.execute(testCase);

            assertThat(response.getStatus().value()).isEqualTo(testCase.getAsserts().getStatus());
            if (testCase.getAsserts().getBody() != null) {
                assertThatJson(response.getResponseBody())
                        .withMatcher("any-zoned-date-time", new AnyZonedDateTimeMatcher())
                        .withMatcher("any-uuid", new AnyUUIDMatcher())
                        .when(IGNORING_EXTRA_FIELDS, IGNORING_ARRAY_ORDER, IGNORING_EXTRA_ARRAY_ITEMS)
                        .isEqualTo(testCase.getAsserts().getBody());
            }
        })).toList();
    }

    private String getFileName(String filename) {
        String env = System.getProperty("env", "");
        return !env.isEmpty() ? String.format("%s/%s", env, filename) : filename;
    }

}