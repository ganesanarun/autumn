package org.autumn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.autumn.reporting.ReportingConfig;
import org.autumn.reporting.TestResultCollector;
import org.autumn.reporting.TestSummaryGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Autumn reporting features.
 * <p>
 * This configuration is automatically activated when the framework is on the classpath.
 * It registers beans for test summary generation using the TestResultCollector.
 * <p>
 * Users can disable test summary generation via application.properties: <pre>
 * autumn.reporting.test-summary.enabled=false
 * </pre>
 */
@Configuration
@EnableConfigurationProperties(ReportingConfig.class)
public class AutumnReportingAutoConfiguration {

	/**
	 * Provides the TestResultCollector bean for collecting test results during execution.
	 */
	@Bean
	public TestResultCollector testResultCollector() {
		return new TestResultCollector();
	}

	/**
	 * Provides the TestSummaryGenerator bean if enabled.
	 */
	@Bean
	@ConditionalOnProperty(prefix = "autumn.reporting.test-summary", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public TestSummaryGenerator testSummaryGenerator(ReportingConfig config, TestResultCollector testResultCollector) {
		ObjectMapper objectMapper = new ObjectMapper();
		return new TestSummaryGenerator(config, objectMapper, testResultCollector);
	}

}
