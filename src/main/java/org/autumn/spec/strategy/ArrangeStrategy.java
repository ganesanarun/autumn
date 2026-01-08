package org.autumn.spec.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.autumn.spec.ArrangeStep;

import java.util.Map;

/**
 * Strategy interface for executing different types of arrange steps.
 * <p>
 * Implementations of this interface define how specific arrange step types (e.g., "api",
 * "wait") are executed during the test setup phase.
 *
 * @see ApiArrangeStrategy
 * @see WaitStrategy
 * @see DefaultStrategy
 */
public interface ArrangeStrategy {

	/**
	 * Executes an arrange step and updates the context.
	 * @param step the arrange step to execute
	 * @param context the current test context that may be modified by the step
	 * @return the result of the arrange step, typically a JSON response body
	 */
	JsonNode arrange(ArrangeStep step, Map<String, JsonNode> context);

}
