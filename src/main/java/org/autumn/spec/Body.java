package org.autumn.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javacrumbs.jsonunit.core.Option;

import java.util.List;

/**
 * Configuration for customizing JSON body comparison behavior.
 * <p>
 * This class wraps JSON Unit comparison options that control how response bodies are
 * compared during assertions, such as ignoring extra fields or array order.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Body {

	private List<Option> options;

}
