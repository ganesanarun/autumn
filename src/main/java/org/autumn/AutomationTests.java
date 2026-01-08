package org.autumn;

import io.qameta.allure.Allure;
import org.autumn.executors.ArrangeExecutor;
import org.autumn.executors.HttpRequestExecutor;
import org.autumn.reporting.TestResultCollector;
import org.autumn.spec.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.opentest4j.TestAbortedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Base class for YAML-driven API automation tests using JUnit 5 dynamic tests.
 * <p>
 * This class automatically discovers and executes test cases defined in YAML files
 * located in the test resources directory. Test classes should extend this class to
 * inherit the test discovery and execution functionality.
 * <p>
 * Example usage: <pre>
 * public class ApiTests extends AutomationTests {
 *     // That's it! Framework will discover and run your YAML tests
 * }
 * </pre>
 * <p>
 * The framework supports environment-specific test execution by setting the "env" system
 * property. For example: {@code -Denv=staging}
 *
 * @see org.autumn.spec.TestCase
 * @see YamlReader
 */
public class AutomationTests {

	private static final HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor(RestClient.create());

	private static final YamlReader yamlReader = new YamlReader();

	private static final ArrangeExecutor arrangeExecutor = new ArrangeExecutor();

	private static final Logger LOGGER = LoggerFactory.getLogger(AutomationTests.class);

	private static String resourcePattern = "classpath*:**/*.y*ml";

	@Autowired(required = false)
	private TestResultCollector testResultCollector;

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
		return Arrays.stream(resources)
			.map(FileMeta::from)
			.collect(Collectors.groupingBy(FileMeta::folderPath,
					Collectors.mapping(this::testFrom, Collectors.toList())));
	}

	private Stream<DynamicNode> create(Map<String, List<DynamicTest>> leafTestsByFolder) {
		Node root = buildNodeTree(leafTestsByFolder);
		return root.children().values().stream().map(this::buildDynamicNode);
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
		Stream<DynamicNode> children = Stream.concat(node.tests().stream(),
				node.children().values().stream().map(this::buildDynamicNode));
		return dynamicContainer(node.name(), children);
	}

	private DynamicTest testFrom(FileMeta testInfo) {
		var testCase = yamlReader.readYamlFile(testInfo.filePath());
		return dynamicTest(testInfo.getTestName(), () -> {
			testCase.annotateFor(testInfo);

			TestResultCollector.TestResult result = TestResultCollector.TestResult.PASSED;

			try {
				if (testCase.shouldBeSkipped()) {
					result = TestResultCollector.TestResult.SKIPPED;
					throw new TestAbortedException(testCase.getDisabledReason());
				}

				var context = arrangeExecutor.execute(testCase.getArrange());
				testCase.setContext(context);

				var response = httpRequestExecutor.execute(testCase);

				testCase.assertThis(response.getStatus());
				testCase.assertThis(response.getResponseBody());
				annotateWith(response);
			}
			catch (TestAbortedException e) {
				// Test was skipped - result already set above
				throw e;
			}
			catch (Throwable e) {
				result = TestResultCollector.TestResult.FAILED;
				throw e;
			}
			finally {
				// Record the test result if collector is available
				if (testResultCollector != null) {
					testResultCollector.recordTestResult(result);
				}
			}
		});
	}

	private void annotateWith(Response response) {
		Allure.step("Response Details", () -> {
			Allure.parameter("Status Code", response.getStatus().toString());
			if (response.getResponseBody() != null) {
				Allure.attachment("Response Body", response.getResponseBody().toPrettyString());
			}
		});
	}

}