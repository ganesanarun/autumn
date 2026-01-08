package org.autumn.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service for generating test execution summaries.
 * <p>
 * This component can generate test summaries in two ways:
 * <ol>
 * <li>From collected test results during execution (primary method)</li>
 * <li>From JUnit XML reports (fallback/utility method)</li>
 * </ol>
 * <p>
 * Example usage: <pre>
 * {@literal @}Autowired
 * private TestSummaryGenerator summaryGenerator;
 *
 * {@literal @}AfterAll
 * static void generateSummary() {
 *     TestSummary summary = summaryGenerator.generateSummary();
 *     summaryGenerator.writeSummaryToFile(summary);
 * }
 * </pre>
 */
@Component
public class TestSummaryGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestSummaryGenerator.class);

	private final ReportingConfig config;

	private final ObjectMapper objectMapper;

	private final TestResultCollector testResultCollector;

	public TestSummaryGenerator(ReportingConfig config, ObjectMapper objectMapper,
			TestResultCollector testResultCollector) {
		this.config = config;
		this.objectMapper = objectMapper;
		this.testResultCollector = testResultCollector;
	}

	/**
	 * Generates a test summary from collected test results (primary method).
	 * @return TestSummary object containing aggregated test results
	 */
	public TestSummary generateSummary() {
		LOGGER.debug("Generating test summary from collected results");
		return testResultCollector.getCurrentSummary();
	}

	/**
	 * Writes the test summary to the configured output file as JSON.
	 * @param summary The test summary to write
	 * @throws IOException if writing fails
	 */
	public void writeSummaryToFile(TestSummary summary) throws IOException {
		writeSummaryToFile(summary, config.getTestSummary().getOutputPath());
	}

	/**
	 * Writes the test summary to the specified file as JSON.
	 * @param summary The test summary to write
	 * @param outputPath Path where the JSON file should be written
	 * @throws IOException if writing fails
	 */
	public void writeSummaryToFile(TestSummary summary, String outputPath) throws IOException {
		Path path = Paths.get(outputPath);
		Files.createDirectories(path.getParent());

		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(summary);
		Files.writeString(path, json);

		LOGGER.info("Test summary written to: {}", path.toAbsolutePath());
	}

	/**
	 * Generates and writes a test summary in one operation.
	 * @return The generated test summary
	 * @throws IOException if writing fails
	 */
	public TestSummary generateAndWrite() throws IOException {
		TestSummary summary = generateSummary();
		writeSummaryToFile(summary);
		return summary;
	}

	private String determineResult(int totalTests, int failedTests) {
		if (failedTests > 0) {
			return "failure";
		}
		if (totalTests == 0) {
			return "unknown";
		}
		return "success";
	}

	private TestSummary buildUnknownSummary() {
		return TestSummary.builder().total(0).passed(0).failed(0).skipped(0).result("unknown").build();
	}

	private record TestCounts(int tests, int failures, int errors, int skipped) {
	}

}