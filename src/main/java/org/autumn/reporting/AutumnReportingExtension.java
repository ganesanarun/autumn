package org.autumn.reporting;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JUnit 5 extension that automatically generates test summaries after all tests complete.
 * <p>
 * This extension integrates with Spring's test context to access the TestSummaryGenerator
 * bean. It will only execute if test summary generation is enabled in the configuration.
 * <p>
 * To use this extension, add it to your test class: <pre>
 * {@literal @}ExtendWith(AutumnReportingExtension.class)
 * public class ApiTests extends AutomationTests {
 *     // Your tests
 * }
 * </pre>
 * <p>
 * Or configure it globally in junit-platform.properties: <pre>
 * junit.jupiter.extensions.autodetection.enabled=true
 * </pre>
 */
public class AutumnReportingExtension implements AfterAllCallback {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutumnReportingExtension.class);

	@Override
	public void afterAll(ExtensionContext context) {
		try {
			ApplicationContext appContext = SpringExtension.getApplicationContext(context);
			ReportingConfig config = appContext.getBean(ReportingConfig.class);

			// Generate test summary if enabled
			if (config.getTestSummary().isEnabled()) {
				TestSummaryGenerator summaryGenerator = appContext.getBean(TestSummaryGenerator.class);
				TestSummary summary = summaryGenerator.generateSummary();
				summaryGenerator.writeSummaryToFile(summary);

				LOGGER.info("Test summary generated successfully from collected results");
			}

		}
		catch (Exception e) {
			LOGGER.error("Failed to execute reporting extension", e);
			// Don't fail the test run if reporting fails
		}
	}

}
