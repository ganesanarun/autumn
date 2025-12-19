package org.andino.autumn.spec.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.andino.autumn.spec.ArrangeStep;
import java.util.Map;

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
