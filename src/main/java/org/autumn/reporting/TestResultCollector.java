package org.autumn.reporting;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Collects test results during test execution for immediate reporting.
 * <p>
 * This component captures test results as they happen during framework execution,
 * eliminating the need to parse XML files later. It provides real-time test statistics
 * that can be used for generating summaries.
 */
@Component
public class TestResultCollector {

	private final AtomicInteger totalTests = new AtomicInteger(0);

	private final AtomicInteger passedTests = new AtomicInteger(0);

	private final AtomicInteger failedTests = new AtomicInteger(0);

	private final AtomicInteger skippedTests = new AtomicInteger(0);

	/**
	 * Records a test execution result.
	 * @param result The test result
	 */
	public void recordTestResult(TestResult result) {
		totalTests.incrementAndGet();

		switch (result) {
			case PASSED -> passedTests.incrementAndGet();
			case FAILED -> failedTests.incrementAndGet();
			case SKIPPED -> skippedTests.incrementAndGet();
		}
	}

	/**
	 * Gets the current test summary based on collected results.
	 * @return TestSummary with current statistics
	 */
	public TestSummary getCurrentSummary() {
		int total = totalTests.get();
		int passed = passedTests.get();
		int failed = failedTests.get();
		int skipped = skippedTests.get();

		String result = failed > 0 ? "failure" : (total > 0 ? "success" : "unknown");

		return TestSummary.builder().total(total).passed(passed).failed(failed).skipped(skipped).result(result).build();
	}

	/**
	 * Resets all counters (useful for test isolation).
	 */
	public void reset() {
		totalTests.set(0);
		passedTests.set(0);
		failedTests.set(0);
		skippedTests.set(0);
	}

	public enum TestResult {

		PASSED, FAILED, SKIPPED

	}

}