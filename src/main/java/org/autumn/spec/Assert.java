package org.autumn.spec;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;
import org.autumn.matchers.AnyUUIDMatcher;
import org.autumn.matchers.AnyZonedDateTimeMatcher;
import org.springframework.http.HttpHeaders;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assert {

	private int status;

	private JsonNode body;

	private HttpHeaders headers;

	private Customization customization;

	/**
	 * Asserts that the actual JSON response body matches the expected body.
	 * <p>
	 * This method uses custom matchers for UUID and ZonedDateTime fields, and applies any
	 * customization options specified in the test case (e.g., ignoring extra fields).
	 * @param actual the actual JSON response body received from the API
	 * @return a JsonAssert object for fluent assertions
	 */
	public JsonAssert assertBody(JsonNode actual) {
		if (customization == null || customization.getBody() == null
				|| customization.getBody().getOptions().isEmpty()) {
			return assertThatJson(actual).withMatcher("any-zoned-date-time", new AnyZonedDateTimeMatcher())
				.withMatcher("any-uuid", new AnyUUIDMatcher())
				.isEqualTo(body);
		}

		return assertThatJson(actual).withMatcher("any-zoned-date-time", new AnyZonedDateTimeMatcher())
			.withMatcher("any-uuid", new AnyUUIDMatcher())
			.when(customization.getBody().getOptions().getFirst(),
					customization.getBody().getOptions().toArray(Option[]::new))
			.isEqualTo(body);
	}

}
