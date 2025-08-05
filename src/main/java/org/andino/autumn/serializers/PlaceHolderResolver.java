package org.andino.autumn.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.regex.Pattern;

import static net.andreinc.mockneat.unit.user.Names.names;

public class PlaceHolderResolver {

	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{generate:([^}]+)\\}");

	private static final DateTimeFormatter isoTimeFormatter;

	static {
		isoTimeFormatter = new DateTimeFormatterBuilder()
				.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				.optionalStart()
				.appendOffsetId().toFormatter();
	}

	public static JsonNode resolve(JsonNode node) {
		if (node.isObject()) {
			ObjectNode objectNode = (ObjectNode) node;
			objectNode.fieldNames().forEachRemaining(field -> {
				JsonNode value = objectNode.get(field);
				if (value.isTextual()) {
					String resolvedValue = PLACEHOLDER_PATTERN.matcher(value.asText()).replaceAll(match ->
							resolvePlaceHolder(match.group(1).toLowerCase(Locale.ENGLISH)).toString());
					objectNode.put(field, resolvedValue);
				} else {
					resolve(value);
				}
			});
		} else if (node.isArray()) {
			node.forEach(PlaceHolderResolver::resolve);
		}
		return node;
	}

	private static Object resolvePlaceHolder(String expr) {
		String[] parts = expr.split(":");
		String key = parts[0];
		return switch (key) {
			case "datetime" -> resolveTimeFor();
			case "string" -> resolveStringFor(parts);
			default -> expr;
		};
	}

	private static Object resolveTimeFor() {
		return ZonedDateTime.now(ZoneId.of("UTC")).format(isoTimeFormatter);
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
