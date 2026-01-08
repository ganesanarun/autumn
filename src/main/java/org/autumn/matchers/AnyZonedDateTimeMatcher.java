package org.autumn.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.ZonedDateTime;

/**
 * Hamcrest matcher that matches any valid ZonedDateTime string.
 * <p>
 * This matcher is used in JSON assertions to verify that a field contains a valid
 * ISO-8601 date-time string without requiring an exact timestamp match. Useful for
 * testing APIs that return timestamps.
 */
public class AnyZonedDateTimeMatcher extends BaseMatcher<Object> {

	@Override
	public boolean matches(Object actual) {
		try {
			ZonedDateTime.parse(actual.toString());
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public void describeTo(Description description) {

	}

	@Override
	public void describeMismatch(Object item, Description description) {
		description.appendText("Expected a valid date time string but got: ").appendValue(item).appendText(" instead.");
	}

}
