package org.andino.autumn.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

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
