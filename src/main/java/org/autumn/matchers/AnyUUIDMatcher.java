package org.autumn.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Hamcrest matcher that matches any valid UUID string.
 * <p>
 * This matcher is used in JSON assertions to verify that a field contains a valid UUID
 * without requiring an exact UUID match. Useful for testing APIs that generate UUIDs.
 */
public class AnyUUIDMatcher extends BaseMatcher<Object> {

	@Override
	public boolean matches(Object actual) {
		try {
			java.util.UUID.fromString(actual.toString());
			return true;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public void describeTo(Description description) {

	}

	@Override
	public void describeMismatch(Object item, Description description) {
		description.appendText("Expected a valid uuid string but got: ").appendValue(item).appendText(" instead.");
	}

}
