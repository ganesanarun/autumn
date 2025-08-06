package org.andino.autumn.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.ZonedDateTime;

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
