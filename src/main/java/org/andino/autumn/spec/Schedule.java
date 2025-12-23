package org.andino.autumn.spec;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Schedule {

	@JsonUnwrapped
	private TimeWindow timeWindow;

	private List<DayOfWeek> applicableDays;

	public boolean isOutsideApplicableDays() {
		if (applicableDays == null || applicableDays.isEmpty()) {
			return false; // No restriction, valid for all days
		}
		DayOfWeek today = java.time.LocalDate.now().getDayOfWeek();
		return !applicableDays.contains(today);
	}

	public boolean isOutsideExecutionWindow() {
		return timeWindow != null && timeWindow.isOutsideExecutionWindow();
	}

}
