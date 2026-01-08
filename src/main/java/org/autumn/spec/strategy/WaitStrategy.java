package org.autumn.spec.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.autumn.spec.ArrangeStep;
import java.util.Map;

/**
 * Arrange a strategy that pauses execution for a specified duration.
 * <p>
 * This strategy is useful for waiting between API calls or allowing time for asynchronous
 * operations to complete during test setup.
 */
public class WaitStrategy implements ArrangeStrategy {

	@Override
	public JsonNode arrange(ArrangeStep step, Map<String, JsonNode> context) {
		if (step.getWaitMillis() != null && step.getWaitMillis() > 0) {
			try {
				Thread.sleep(step.getWaitMillis());
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Interrupted during wait arrange step", e);
			}
		}
		return null;
	}

}
