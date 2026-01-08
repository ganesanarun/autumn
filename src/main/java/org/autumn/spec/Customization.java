package org.autumn.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Customization options for test assertions.
 * <p>
 * This class allows customizing how different parts of the response are compared,
 * currently supporting body comparison customizations.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customization {

	private Body body;

}
