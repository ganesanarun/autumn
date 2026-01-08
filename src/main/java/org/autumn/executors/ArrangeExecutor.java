package org.autumn.executors;

import com.fasterxml.jackson.databind.JsonNode;
import org.autumn.spec.ArrangeStep;
import org.autumn.spec.strategy.ApiArrangeStrategy;
import org.autumn.spec.strategy.ArrangeStrategy;
import org.autumn.spec.strategy.DefaultStrategy;
import org.autumn.spec.strategy.WaitStrategy;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Executes the "Arrange" phase of test cases, setting up test data and context.
 * <p>
 * This executor processes a list of arrange steps, delegating to appropriate strategy
 * implementations based on the step type (e.g., "api", "wait").
 */
public class ArrangeExecutor {

	/**
	 * Executes a list of arrange steps and builds a context map.
	 * @param steps the list of arrange steps to execute
	 * @return a map containing the context variables created during arrangement
	 */
	public Map<String, JsonNode> execute(List<ArrangeStep> steps) {
		Map<String, JsonNode> context = new HashMap<>();
		for (ArrangeStep step : steps) {
			findExecutor(step.getType()).arrange(step, context);
		}
		return new HashMap<>(context);
	}

	private ArrangeStrategy findExecutor(String type) {
		return switch (type) {
			case "api" -> new ApiArrangeStrategy(new HttpRequestExecutor(RestClient.create()));
			case "wait" -> new WaitStrategy();
			default -> new DefaultStrategy();
		};
	}

}
