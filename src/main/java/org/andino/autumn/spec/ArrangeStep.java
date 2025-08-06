package org.andino.autumn.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

}
