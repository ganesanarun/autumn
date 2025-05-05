package org.andino.autumn;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URI;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AutomationTests {

    private final HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor(RestClient.create(), new ObjectMapper());
    private final YamlReader yamlReader = new YamlReader();
    private static final Logger LOGGER = LoggerFactory.getLogger(AutomationTests.class);
    private static String resourcePattern = "classpath*:**/*.y*ml";

    @BeforeAll
    public static void setup() {
        String env = System.getProperty("env", "");
        LOGGER.info("Using environment: {}", env);
        if (!env.isEmpty()) {
            resourcePattern = "classpath*:" + env + "/**/*.y*ml";
        }
        LOGGER.info("Loading resource pattern: {}", resourcePattern);
    }

    @TestFactory
    public Collection<DynamicTest> tests() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(resourcePattern);

        return Stream.of(resources).parallel().map(resource -> dynamicTest(getFileName(resource), () -> {
            var fileName = getFileName(resource.getURI());
            TestCase testCase = yamlReader.readYamlFile(fileName);

            Response response = httpRequestExecutor.execute(testCase);

            testCase.assertThis(response.getStatus());
            testCase.assertThis(response.getResponseBody());
        })).toList();
    }

    private String getFileName(URI uri) {
        return uri.toString().substring(uri.toString().lastIndexOf("/resources/test/") + 16);
    }


    private String getFileName(Resource resource) {
        try {
            return getFileName(resource.getURI());
        } catch (IOException e) {
            throw new RuntimeException("Failed to get file name from resource", e);
        }
    }

}