package org.autumn.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a single step in the "Arrange" phase of a test case.
 * <p>
 * Arrange steps are used to set up test data or state before the main test action. They
 * can make API calls, wait for a duration, or perform other setup tasks. Results can be
 * saved to the test context for use in subsequent steps or assertions.
 */
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ArrangeStep {

	private String name;

	private String type;

	private Act act;

	private String saveResponseAs;

	private Long waitMillis;

}
