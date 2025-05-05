package org.andino.autumn;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.opentest4j.TestAbortedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class AutomationTests {

    private static final HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor(RestClient.create(), new ObjectMapper());
    private static final YamlReader yamlReader = new YamlReader();
    private static final Logger LOGGER = LoggerFactory.getLogger(AutomationTests.class);
    private static final String TEST_RESOURCE_PATH = "/resources/test/";
    private static String resourcePattern = "classpath*:**/*.y*ml";

    @BeforeAll
    static void setup() {
        String env = System.getProperty("env", "");
        LOGGER.info("Using environment: {}", env);
        if (!env.isEmpty()) {
            resourcePattern = "classpath*:" + env + "/**/*.y*ml";
        }
        LOGGER.info("Loading resource pattern: {}", resourcePattern);
    }

    @TestFactory
    Stream<DynamicNode> tests() throws Exception {
        var resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(resourcePattern);

        var testsByFolder = leafTestsByFolder(resources);

        return create(testsByFolder);
    }

    private Map<String, List<DynamicTest>> leafTestsByFolder(Resource[] resources) {
        return Arrays.stream(resources).map(this::createTestInfo).collect(Collectors.groupingBy(TestInfo::folderPath, Collectors.mapping(this::testFrom, Collectors.toList())));
    }

    private TestInfo createTestInfo(Resource resource) {
        String filePath = getResourcePath(resource);
        String folderPath = getFolderPath(filePath);
        return new TestInfo(getFileName(filePath), filePath, folderPath);
    }

    private Stream<DynamicNode> create(Map<String, List<DynamicTest>> leafTestsByFolder) {
        Node root = buildNodeTree(leafTestsByFolder);
        return root.children.values().stream().map(this::buildDynamicNode);
    }

    private Node buildNodeTree(Map<String, List<DynamicTest>> leafTestsByFolder) {
        Node root = new Node("root");
        for (var entry : leafTestsByFolder.entrySet()) {
            String folderPath = entry.getKey();
            String[] folderNames = folderPath.split("/");

            Node currentNode = root;
            for (String folderName : folderNames) {
                currentNode = currentNode.getOrCreateChild(folderName);
            }
            currentNode.addTests(entry.getValue());
        }
        return root;
    }

    private DynamicNode buildDynamicNode(Node node) {
        Stream<DynamicNode> children = Stream.concat(node.tests.stream(), node.children.values().stream().map(this::buildDynamicNode));
        return dynamicContainer(node.name, children);
    }

    private DynamicTest testFrom(TestInfo testInfo) {
        var testCase = yamlReader.readYamlFile(testInfo.filePath);

        if (testCase.isDisabled()) {
            return dynamicTest(testInfo.getTestName(), () -> {
                LOGGER.info("Skipping test: {}", testInfo.getTestName());
                throw new TestAbortedException(testCase.getDisabledReason());
            });
        }

        return dynamicTest(testInfo.getTestName(), () -> {
            var response = httpRequestExecutor.execute(testCase);

            testCase.assertThis(response.getStatus());
            testCase.assertThis(response.getResponseBody());
        });
    }

    private String getFolderPath(String filePath) {
        var path = Paths.get(filePath);
        var parentPath = path.getParent();
        return parentPath == null ? "" : parentPath.toString().replace('\\', '/');
    }

    private String getResourcePath(Resource resource) {
        try {
            var path = resource.getURI().toString();
            var resourcesIndex = path.lastIndexOf(TEST_RESOURCE_PATH);
            return resourcesIndex == -1 ? resource.getFilename() : path.substring(resourcesIndex + 16);
        } catch (IOException e) {
            LOGGER.error("Error getting resource path", e);
            throw new RuntimeException(e);
        }
    }

    private String getFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }

    private record TestInfo(String fileName, String filePath, String folderPath) {

        public String getTestName() {
            return fileName.replace(".yml", "").replace(".yaml", "");
        }
    }

    private record Node(String name, List<DynamicTest> tests, Map<String, Node> children) {
        public Node(String name) {
            this(name, new ArrayList<>(), new HashMap<>());
        }

        public Node getOrCreateChild(String name) {
            return children.computeIfAbsent(name, Node::new);
        }

        public void addTests(List<DynamicTest> testsToAdd) {
            this.tests.addAll(testsToAdd);
        }
    }
}
