package org.autumn.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a summary of test execution results.
 * <p>
 * This model captures the aggregate statistics from a test run including the total number
 * of tests, passed, failed, and skipped tests, along with an overall result status.
 * <p>
 * Example JSON output: <pre>
 * {
 *   "total": 10,
 *   "passed": 8,
 *   "failed": 1,
 *   "skipped": 1,
 *   "result": "failure"
 * }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSummary {

	/**
	 * Total number of tests executed
	 */
	@JsonProperty("total")
	private int total;

	/**
	 * Number of tests that passed
	 */
	@JsonProperty("passed")
	private int passed;

	/**
	 * Number of tests that failed
	 */
	@JsonProperty("failed")
	private int failed;

	/**
	 * Number of tests that were skipped
	 */
	@JsonProperty("skipped")
	private int skipped;

	/**
	 * Overall result: "success", "failure", or "unknown"
	 */
	@JsonProperty("result")
	private String result;

}
