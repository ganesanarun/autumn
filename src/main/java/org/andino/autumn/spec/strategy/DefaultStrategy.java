package org.andino.autumn.spec.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.andino.autumn.spec.ArrangeStep;

import java.util.Map;

public record DefaultStrategy() implements ArrangeStrategy {
	@Override
	public JsonNode arrange(ArrangeStep step, Map<String, JsonNode> context) {
		return null;
	}
}
