package org.autumn;

import org.junit.jupiter.api.DynamicTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Node(String name, List<DynamicTest> tests, Map<String, Node> children) {
	public Node(String name) {
		this(name, new ArrayList<>(), new HashMap<>());
	}

	public Node getOrCreateChild(String name) {
		return children.computeIfAbsent(name, Node::new);
	}

	/**
	 * Adds tests to this node's test collection.
	 * @param testsToAdd the tests to add to this node
	 */
	public void addTests(List<DynamicTest> testsToAdd) {
		this.tests.addAll(testsToAdd);
	}
}
