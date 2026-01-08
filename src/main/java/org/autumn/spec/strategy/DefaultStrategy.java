package org.autumn.spec.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.autumn.spec.ArrangeStep;

import java.util.Map;

/**
 * Default no-op arrange strategy used when no specific strategy is matched.
 * <p>
 * This strategy does nothing and returns null. It serves as a fallback for unknown or
 * unimplemented arrange step types.
 */
public record DefaultStrategy() implements ArrangeStrategy {
	@Override
	public JsonNode arrange(ArrangeStep step, Map<String, JsonNode> context) {
		return null;
	}
}
