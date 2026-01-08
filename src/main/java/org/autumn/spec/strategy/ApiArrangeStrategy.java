package org.autumn.spec.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.autumn.executors.HttpRequestExecutor;
import org.autumn.serializers.PlaceHolderResolver;
import org.autumn.spec.ArrangeStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

/**
 * Arrange strategy that executes HTTP API calls during test setup.
 * <p>
 * This strategy makes HTTP requests as defined in the arrange step, resolves placeholders
 * in the request body, and optionally saves the response to the test context for use in
 * subsequent steps or assertions.
 *
 * @param executor the HTTP request executor to use for making API calls
 */
public record ApiArrangeStrategy(HttpRequestExecutor executor) implements ArrangeStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiArrangeStrategy.class);

	@Override
	public JsonNode arrange(ArrangeStep step, Map<String, JsonNode> context) {
		try {
			JsonNode resolvedBody = PlaceHolderResolver.resolve(step.getAct().getBody(), context);
			LOGGER.debug("Resolved Body: {} with context {}", resolvedBody, context);
			var headers = new HttpHeaders();
			if (step.getAct() != null && step.getAct().getHeaders() != null) {
				headers.addAll(step.getAct().getHeaders());
			}
			var response = executor.execute(step.getAct().withBody(resolvedBody), headers);
			if (step.getSaveResponseAs() != null && !step.getSaveResponseAs().isBlank()) {
				context.put(step.getSaveResponseAs(), response.getResponseBody());
			}
			return response.getResponseBody();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error arranging API step: " + step.getName(), ex);
		}
	}
}
