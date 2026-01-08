package org.autumn.reporting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for test reporting features.
 * <p>
 * These properties can be configured in application.properties or application.yml: <pre>
 * autumn.reporting.test-summary.enabled=true
 * autumn.reporting.test-summary.output-path=build/test-results/summary.json
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "autumn.reporting")
public class ReportingConfig {

	private TestSummaryConfig testSummary = new TestSummaryConfig();

	@Data
	public static class TestSummaryConfig {

		/**
		 * Whether test summary generation is enabled
		 */
		private boolean enabled = true;

		/**
		 * Output path for the JSON summary file
		 */
		private String outputPath = "build/test-results/summary.json";

	}

}
