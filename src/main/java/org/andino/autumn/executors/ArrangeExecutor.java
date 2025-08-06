package org.andino.autumn.executors;

import com.fasterxml.jackson.databind.JsonNode;
import org.andino.autumn.spec.ArrangeStep;
import org.andino.autumn.spec.strategy.ApiArrangeStrategy;
import org.andino.autumn.spec.strategy.ArrangeStrategy;
import org.andino.autumn.spec.strategy.DefaultStrategy;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrangeExecutor {

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
			default -> new DefaultStrategy();
		};
	}

}
