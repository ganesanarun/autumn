package org.andino.autumn.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;

@Getter
public class TimeWindow {

	private final LocalTime activeAfter;

	private final LocalTime activeBefore;

	private final ZoneId timeZone;

	@JsonCreator
	public TimeWindow(@JsonProperty("activeAfter") LocalTime activeAfter,
			@JsonProperty("activeBefore") LocalTime activeBefore, @JsonProperty("timezone") String timeZone) {
		this.activeAfter = activeAfter == null ? LocalTime.MIN : activeAfter;
		this.activeBefore = activeBefore == null ? LocalTime.MAX : activeBefore;
		this.timeZone = Optional.ofNullable(timeZone).map(ZoneId::of).orElse(UTC);
	}

	public boolean isOutsideExecutionWindow() {
		LocalTime now = LocalTime.now(timeZone);
		return now.isBefore(activeAfter) || now.isAfter(activeBefore);
	}

}
