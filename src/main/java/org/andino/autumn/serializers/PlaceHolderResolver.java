package org.andino.autumn.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.andreinc.mockneat.unit.user.Names.names;

public class PlaceHolderResolver {

	private static final Pattern GENERATE_PATTERN = Pattern.compile("\\$\\{generate:([^}]+)\\}");

	private static final Pattern CONTEXT_PATTERN = Pattern.compile("\\$\\{([^.}]+)\\.([^}]+)\\}");

	private static final DateTimeFormatter isoTimeFormatter;

	static {
		isoTimeFormatter = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
			.optionalStart()
			.appendOffsetId()
			.toFormatter();
	}

	public static JsonNode resolve(JsonNode node, Map<String, JsonNode> context) {
		if (node == null || context == null) {
			return node;
		}

		if (node.isTextual() && node.asText().startsWith("&")) {
			var contextKey = node.asText().substring(1);
			return context.getOrDefault(contextKey, JsonNodeFactory.instance.nullNode());
		}

		return resolveComplexObject(node, context);
	}

	private static JsonNode resolveComplexObject(JsonNode node, Map<String, JsonNode> context) {
		if (node.isObject()) {
			ObjectNode objectNode = (ObjectNode) node;
			objectNode.fieldNames().forEachRemaining(field -> {
				JsonNode value = objectNode.get(field);
				if (value.isTextual()) {
					String resolvedValue = resolveTextValue(value.asText(), context);
					objectNode.put(field, resolvedValue);
				}
				else {
					resolve(value, context);
				}
			});
		}
		else if (node.isArray()) {
			node.forEach(element -> resolve(element, context));
		}
		return node;
	}

	private static String resolveTextValue(String text, Map<String, JsonNode> context) {
		if (text == null || text.isEmpty()) {
			return text;
		}

		if (CONTEXT_PATTERN.matcher(text).matches()) {
			return CONTEXT_PATTERN.matcher(text).replaceAll(match -> {
				String contextKey = match.group(1);
				String fieldPath = match.group(2);
				return resolveContextReference(contextKey, fieldPath, context);
			});
		}

		return GENERATE_PATTERN.matcher(text)
			.replaceAll(match -> resolveGenerate(match.group(1).toLowerCase(Locale.ENGLISH)).toString());
	}

	private static String resolveContextReference(String contextKey, String fieldPath, Map<String, JsonNode> context) {
		JsonNode contextNode = context.get(contextKey);
		if (contextNode == null) {
			return JsonNodeFactory.instance.nullNode().asText();
		}
		if (fieldPath == null || fieldPath.isEmpty()) {
			return contextNode.asText();
		}
		JsonNode result = resolvePath(contextNode, fieldPath);
		if (result.isMissingNode() || result.isNull()) {
			return JsonNodeFactory.instance.nullNode().asText();
		}
		return result.isTextual() ? result.asText() : result.toString();
	}

	private static JsonNode resolvePath(JsonNode node, String path) {
		if (node == null || path == null || path.isEmpty())
			return JsonNodeFactory.instance.nullNode();

		String[] parts = path.split("\\.");
		for (String part : parts) {
			Matcher arrayMatcher = Pattern.compile("([a-zA-Z0-9_]+)\\[(\\d+)]").matcher(part);
			if (arrayMatcher.matches()) {
				String arrayField = arrayMatcher.group(1);
				int index = Integer.parseInt(arrayMatcher.group(2));
				node = node.path(arrayField);
				if (node.isArray() && index < node.size()) {
					node = node.get(index);
				}
				else {
					return JsonNodeFactory.instance.nullNode();
				}
			}
			else {
				node = node.path(part);
			}
		}
		return node;
	}

	private static Object resolveGenerate(String expr) {
		String[] parts = expr.split(":");
		String key = parts[0];
		return switch (key) {
			case "datetime" -> resolveTimeFor(parts);
			case "string" -> resolveStringFor(parts);
			default -> expr;
		};
	}

	private static Object resolveTimeFor(String[] parts) {
		ZonedDateTime time = ZonedDateTime.now(ZoneId.of("UTC"));
		if (parts.length >= 2) {
			var modifier = parts[1].toLowerCase(Locale.ENGLISH);
			var isAgo = modifier.endsWith("ago");
			var isAfter = modifier.endsWith("after");

			if (isAfter || isAgo) {
				var amount = modifier.replaceAll("[^0-9]", "").isEmpty() ? 1
						: Integer.parseInt(modifier.replaceAll("[^0-9]", ""));
				var unit = modifier.replaceAll("[0-9]", "").replace("ago", "").replace("after", "").toUpperCase();
				time = isAfter ? time.plus(amount, ChronoUnit.valueOf(unit))
						: time.minus(amount, ChronoUnit.valueOf(unit));
			}
		}
		return time.format(isoTimeFormatter);
	}

	private static Object resolveStringFor(String[] parts) {
		if (parts.length < 2) {
			return "defaultString";
		}
		String type = parts[1];
		return switch (type) {
			case "uuid" -> java.util.UUID.randomUUID().toString();
			case "filename" -> names().first().list(2).get().getFirst();
			default -> "Unsupported string type";
		};
	}

}
