package org.autumn.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Utility class for generating Autumn reports from Gradle tasks.
 * <p>
 * This class provides static methods that can be called from Gradle build scripts to
 * generate test summaries without requiring Spring context or complex configuration.
 * <p>
 * Example usage in build.gradle: <pre>
 * tasks.named('test') {
 *     doLast {
 *         org.autumn.reporting.AutumnReportingUtils.generateTestSummary(
 *             file("build/test-results/summary.json").absolutePath
 *         )
 *     }
 * }
 * </pre>
 * <p>
 * Note: This utility is primarily for legacy/standalone usage. When using the framework
 * with Spring Boot, test summaries are generated automatically via TestResultCollector.
 */
public class AutumnReportingUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutumnReportingUtils.class);

	/**
	 * Generates a test summary using TestResultCollector.
	 * <p>
	 * Note: This method is for standalone usage. When using Spring Boot integration, test
	 * summaries are generated automatically from TestResultCollector.
	 * @param outputPath Path where the JSON summary should be written
	 * @throws IOException if writing JSON fails
	 */
	public static void generateTestSummary(String outputPath) throws IOException {
		LOGGER.info("Generating test summary to: {}", outputPath);

		// Create a minimal configuration for the generator
		ReportingConfig config = createMinimalConfig(outputPath);
		ObjectMapper objectMapper = new ObjectMapper();
		TestResultCollector collector = new TestResultCollector(); // Empty collector for
																	// utility usage

		TestSummaryGenerator generator = new TestSummaryGenerator(config, objectMapper, collector);

		// For utility usage, we generate empty summary since TestResultCollector won't
		// have data
		LOGGER.warn("Utility-based test summary generation produces empty results. "
				+ "Use Spring Boot integration with TestResultCollector for proper results.");

		// Generate empty summary as fallback
		TestSummary emptySummary = TestSummary.builder()
			.total(0)
			.passed(0)
			.failed(0)
			.skipped(0)
			.result("unknown")
			.build();

		generator.writeSummaryToFile(emptySummary, outputPath);

		LOGGER.info("Empty test summary generated. Use Spring Boot integration for proper results.");
	}

	private static ReportingConfig createMinimalConfig(String outputPath) {
		return new ReportingConfig() {
			@Override
			public TestSummaryConfig getTestSummary() {
				return new TestSummaryConfig() {
					@Override
					public boolean isEnabled() {
						return true;
					}

					@Override
					public String getOutputPath() {
						return outputPath;
					}
				};
			}
		};
	}

}